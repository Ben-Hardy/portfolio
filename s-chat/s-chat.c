/*
Assignment 2
Ben Hardy, Jessie H


*/
#include <stdio.h>
#include <errno.h>
#include <strings.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#include "list.h"
#include "rtthreads.h"
#include "RttCommon.h"

#define ERR -1

/* used for select()*/
#define STDIN 0

/* the defined maximum namelength for a machine. Most computers aren't thing
 * long of named */
#define MAXNAMESIZE 250



/* structure to store the info for their machine. We will be using this
 * to get the IP address of the other computer.
 */
struct hostent *theirMachine;

/* The IP address of the computer that the program will be sending messages
 * to.
 */
struct in_addr *theirIP;

/* The name of their machine. Set to have a maximum size of MAXNAMESIZE */
char theirName[MAXNAMESIZE];

/* The port we will be listening from */
int ourport;

/* the port we will be sending messages to */
int theirport;

/* The Thread IDs for all of the threads. We have them as global so that
 * we can easily check them within the admin thread
 */
RttThreadId input, UDPcatcher, printer, sender, admin;

/* the handle in which will be used for listening and sending*/
int servHandle;

/* The printables list will store all strings to be printed. */
LIST *printables;

/* The sendables list will store all strings that will be sent
 * to the other machine
 */
LIST *sendables;


/*
 * The keyboard thread for reading input from.
 * After reading input, it sends the read in input to the administrator
 * thread, which in turn sends it to the sender thread.
 */
RTTTHREAD inputThread(void* inputs) {
        extern RttThreadId admin;
        
        void *data;
        char buf[128];
        int maxlen = 128;
        unsigned int len = 2;
        int err;
        /* used for select. */
        struct timeval timer;
        fd_set reader;
        /*char str[] = "go";*/
        int i;
        

        data = (void*) malloc (1000);
        if (data == NULL) {
            printf("could not allocate storage pointer\n");
            exit(1);
        }



    for (;;) {
        /* zero out the buffer so we don't get residual strings sent */
        bzero(buf, maxlen);
        i = 0;
        /* will timeout and iterate every 5 seconds */
        timer.tv_sec = 5;
        timer.tv_usec = 0;
        /* zero out the reader file descriptor */
        FD_ZERO(&reader);

        /* set reader file descriptor to look for standard input */
        FD_SET(STDIN, &reader);

        /* set up select using our reader and timer we set up*/
        err = select(STDIN + 1, &reader, NULL, NULL, &timer);

        if (err > 0) {
#ifdef TEST
        write(1, "input: keyboard input read\n", 27);
#endif
            while ((buf[i] = getchar()) != '\n' && i < maxlen) 
                i++;
            
            /* if there was actually something in the buffer, add a newline */
            if (strlen(buf) > 0) {
#ifdef TEST
        write(1, "input: input is valid\n", 23);
#endif
                if (strcmp(buf, "exit\n") == 0) {
                    fcntl(servHandle, F_SETFL, ~O_NONBLOCK);
                    write(1, "exiting chat!\n", 14);
                    exit(0);
                }
                /*i++;
                buf[i] = '\n';*/
                len = strlen(buf) + 1;
            /* enqueue the the string in our queue of strings to be sent out.*/
                err = ListPrepend(sendables, (void*)buf);
                if (err == -1) {
                    printf("Error adding to sendable queue\n");
                    exit(1);
                }
#ifdef TEST
        write(1, "input: input added to send list\n", 31);
#endif
        /* signal the administrator thread that there are things to be sent */
                RttSend(admin, &buf, len, &buf, &len);
#ifdef TEST
        write(1, "input: admin notified of item to send\n", 38);
#endif
            }
        }
        RttUSleep(1);    
    }

}

/*
 * The UPDcatcher thread creates a socket for listening for datagrams.
 * After receiving a datagram, the thread will send the string contents
 * of the datagram to the administrator thread, which will in turn send
 * it to the printer thread which will output the text to the screen.
 */
RTTTHREAD UDPcatcherThread(void* inputs) {
    
    extern int servHandle;
    extern int ourport;
    extern RttThreadId admin;
    
    int servlen, err;
    char buf[512];
    unsigned int len = 2;
    struct sockaddr_in ourserver, recvaddr;
    socklen_t recvlen;
    
    recvlen = sizeof(struct sockaddr_in);

    /* server address settings based on the suggested ones in
       Beej's Guide To Networking.
    */
    ourserver.sin_addr.s_addr = INADDR_ANY;
    ourserver.sin_port = htons(ourport);
    ourserver.sin_family = AF_INET;
    memset(ourserver.sin_zero, '\0', sizeof(ourserver.sin_zero));


#ifdef TEST
        write(1, "catcher: creating socket file descriptor\n", 41);
#endif
    
    /* create a socket for use with the internet and is to handle UDP datagrams
     */
    servHandle = socket(AF_INET, SOCK_DGRAM, 0);
    if (servHandle == ERR) {
        printf("Error creating our listening server!\n");
        exit(1);
    }
#ifdef TEST
        write(1, "catcher: socket created\n", 26);
#endif
    servlen = sizeof(ourserver);
#ifdef TEST
        write(1, "catcher: binding socket\n", 24);
#endif
    /* bind our socket to the address we will be listening at */
    err = bind(servHandle, (struct sockaddr *) &ourserver, servlen);
    if (err <= ERR) {
        printf("Error binding our listening server!\n");
        exit(1);
    }
#ifdef TEST
        write(1, "catcher: socket bound\n", 22);
#endif
#ifdef TEST
        write(1, "catcher: setting fcntl\n", 22);
#endif
    
    err = fcntl(servHandle, F_SETFL, O_NONBLOCK);
    if (err <= ERR) {
        printf("Error setting fcntl!\n");
        exit(1);
    }
#ifdef TEST
        write(1, "catcher: fcntl set\n", 19);
#endif

    for (;;) {

        err = recvfrom(servHandle, buf, 128, 0, (struct sockaddr *)&recvaddr,
                       &recvlen);
        
        if (err > 0) {
            err = ListPrepend(printables, (void*) buf);
#ifdef TEST
        write(1, "catcher: message received. adding to list\n", 42);
#endif
            if (err == -1) {
                printf("error prepending to printables queue.\n");
                exit(1);
            }
#ifdef TEST
        write(1, "catcher: message added to list\n", 32);
        write(1, "catcher: notifying administrator\n", 33);
#endif
            len = strlen(buf) + 1;
            RttSend(admin, (void*)&buf, len, (void*)&buf, &len);
#ifdef TEST
        write(1, "catcher: admin successfully notified\n", 37);
#endif
        }
        
        RttUSleep(1);
    }



}

/*
 * The printer thread waits until it gets a signal from the administrator.
 * In this case a signal means that there is text to be printed out to the
 * screen, which the printer will fetch from the global list of strings to
 * print. From here it will print until the list is emptied out and then goes
 * back to waiting for input.
 */
RTTTHREAD printerThread(void* inputs) {
        
        extern LIST *printables;
        extern char theirName[MAXNAMESIZE];

        RttThreadId tempID;
        void *data = (void*) malloc(1000);
        char *item = (char*) malloc(1000);
        unsigned int len;

        /* namelen will be used to output the name of the other machine during
         * chat. */
        int namelen = strlen(theirName);

        for (;;) {
            /* if a message is waiting, get it */
            if (RttMsgWaits()) {
#ifdef TEST
        write(1, "printer: message received. printing\n", 36);
#endif
                RttReceive(&tempID, data, &len);
            
            /* empty out the list of things needing to be printed
             * since there may be a backlog because of network activity.
             * Administrator is not replied to until the backlog is emptied.
             */
                while (ListCount(printables) > 0) {
                    item = (char*) ListTrim(printables);
                    write(1, theirName, namelen);
                    write(1, ": ", 2);
                    write(1, item, strlen(item) + 1);
                }
                RttReply(tempID, NULL, 0);
#ifdef TEST
        write(1, "printer: done printing\n", 23);
#endif
        }
        RttUSleep(1);
    }
}

/* the Sender thread's sole job is to, upon receiving a signal from the admin
 * thread, send a UDP datagram containing a message text that was read by
 * the keyboard input thread and stored in the list of sendables.
 */
RTTTHREAD senderThread(void* inputs) {
        
        extern struct hostent *theirMachine;
        extern LIST *sendables;
        extern int theirport;

        struct sockaddr_in oursock;
        int servHandle;
        int err;
        unsigned int len;
        char *buf = (char*) malloc(1000);
        void* item = (void*) malloc(1000);
        socklen_t sockaddrlen;
        RttThreadId tempID;

        /* get the size of a socket address for later on.*/
        sockaddrlen = sizeof(struct sockaddr_in);
       
        /* set the port we are using to be their port */
        oursock.sin_port = htons(theirport);

        /* set the the socket to be an internet socket */
        oursock.sin_family = AF_INET;

        /* copy the IP address we got in pmain into the socket so we can
         * send it messages. We will use bcopy but memset works too.*/
        bcopy( (char*) theirMachine->h_addr, (char*) &oursock.sin_addr,
                 theirMachine->h_length);
        
    servHandle = socket(AF_INET, SOCK_DGRAM, 0);
    if (servHandle == ERR) {
        printf("Error creating our listening server!\n");
        exit(1);
    }
        for (;;) {
            if (RttMsgWaits()) {
#ifdef TEST
        write(1, "sender: message received. Sending\n", 33);
#endif
            RttReceive(&tempID, item, &len);

                while (ListCount(sendables) > 0) {
                    buf = (char*) ListTrim(sendables);
                    err = sendto(servHandle, buf, strlen(buf) + 2, 0, 
                            (const struct sockaddr*) &oursock, sockaddrlen);
                    if (err < 0) printf("error sending");
                }
                RttReply(tempID, item, len);
                
#ifdef TEST
        write(1, "sender: messages successfully sent\n", 36);
#endif
            }
            RttUSleep(1);
        }
}


/* the administrator/server thread controls the entire chat operation. It
 * waits for signals from other threads and then in turn signals threads 
 * telling them to do operations when need be. */
RTTTHREAD adminThread(void* inputs) {
        extern RttThreadId input, UDPcatcher, printer, sender, admin;
        RttThreadId tempID;
        char *item;
        unsigned int len;
        extern LIST* printables, *sendables;
        void *storage = malloc(1000);
        /* allocate our lists */
        printables = ListCreate();
        sendables = ListCreate();
        if (printables == NULL) {
            printf("unsuccessfully created list!\n");
            exit(1);
        }

        for (;;) {
            if (RttMsgWaits()) {
#ifdef TEST
        write(1, "Message available for admin, receiving\n", 40);
#endif
                RttReceive(&tempID, storage, &len);
                item = (char*)storage;
                /*write(1, item, len);
                write(1, (char*) strlen(item), 3);*/
    
#ifdef TEST
        write(1, "Replying in admin thread\n", 25);
#endif
                RttReply(tempID, item, len);
#ifdef TEST
        write(1, "admin replied\n", 14);
#endif
                /* initially, things were set up so that
                 * prepending was done in the admin thread,
                 * but it ended up being not terribly reliable
                 * so it was moved to be done in the input
                 * and catcher threads */
                if (RTTTHREADEQUAL(tempID, input)) {
                        /*ListPrepend(sendables, item);*/
                        RttSend(sender, &item, len, &item, &len);
                }
                else if (RTTTHREADEQUAL(tempID, UDPcatcher)) {
                        /*ListPrepend(printables, item);*/
                        RttSend(printer, &item, len, &item, &len);
                }
#ifdef TEST
        write(1, "Sent message to relevant thread\n", 32);
#endif
            }
            RttUSleep(1);
        }


}

int mainp( int argc, char* argv[] )
{

        extern struct hostent *theirMachine;
        extern int ourport, theirport;
        extern char theirName[MAXNAMESIZE];
        extern struct in_addr *theirIP;
       
        int err;
        RttSchAttr attributes;
        extern RttThreadId input, UDPcatcher, printer, sender, admin;
    /* we expect 3 arguments. */

    if (argc != 4) {
        printf("\nWrong number of arguments!\n");
        return -1;
    
    } else {

        /* port numbers must fall within range. */
#ifdef TEST
            printf("test def worked!\n");
#endif
            if (atoi(argv[1]) <= 30001 || atoi(argv[1]) >= 40000) {
            printf("\nOur port number out of range.\n");
            return -1;
        }
                ourport = atoi(argv[1]);
        /* their machine can't be null. */

        if (strcmp(argv[2], "")== 0) {
            printf("\nInvalid machine name.\n");
            return -1;
        }
                strcpy(theirName, argv[2]);
        /* port numbers must fall within range. */

        if (atoi(argv[3]) <= 30001 || atoi(argv[3]) >= 40000) {
            printf("\nTheir port number out of range.\n");
            return -1;
        }
                theirport = atoi(argv[3]);
                /* set attributes for the threads */
                attributes.startingtime = RTTZEROTIME;
                attributes.priority = RTTHIGH;
                attributes.deadline = RTTNODEADLINE;
                

                theirMachine = gethostbyname(theirName);
                if (theirMachine == NULL) {
                     printf("Unsuccessfully got machine info\n");
                        exit(1);
                 }
                printf("Their Machine:\nName: %s\n", theirMachine->h_name);
                
                theirIP = (struct in_addr*) theirMachine->h_addr_list;

                printf("Their Machine IP Address: %s\n", inet_ntoa(*theirIP));
               
                printf("\n\nType \"exit\" to quit chat.\n\n");


                err = RttCreate(&admin, adminThread, 160000, "admin", argv, 
                        attributes, RTTUSR);
                if (err != RTTOK) {
                    printf("Failure to create input thread\n");
                    exit(1);
                }
                RttUSleep(100); /* to make sure Admin ID is set */
                err = RttCreate(&input, inputThread, 160000, "input", argv, 
                         attributes, RTTUSR);
                if (err != RTTOK) {
                    printf("Failure to create input thread\n");
                    exit(1);
                }
                err = RttCreate(&printer, printerThread, 160000, "printer", 
                        argv, attributes, RTTUSR);
                if (err != RTTOK) {
                    printf("Failure to create printer thread\n");
                    exit(1);
                }
                err = RttCreate(&sender, senderThread, 160000, "sender", argv, 
                        attributes, RTTUSR);
                if (err != RTTOK) {
                    printf("Failure to create sender thread\n");
                    exit(1);
                }
                err =RttCreate(&UDPcatcher, UDPcatcherThread, 160000, 
                        "UDPcatcher", argv, attributes, RTTUSR);
                if (err != RTTOK) {
                    printf("Failure to create catcher thread\n");
                    exit(1);
                }

    }

    return 0;
}

