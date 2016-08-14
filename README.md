Timecrypt Server
================

General information
-------------------

This server implementation uses Java. You can write your own in any other language, and if you
want to do so, you shouldn't continue reading this page. This page contains information on how
to set up your private Timecrypt server using the same configuration we used for the demo.

This configuration consists of:

1. Linux OS, Santiago 6.7
```bash
$ uname -a
Linux ex-std-node789.prod.rhcloud.com 2.6.32-573.12.1.el6.x86_64 #1 SMP Mon Nov 23 12:55:32 EST 2015 x86_64 x86_64 x86_64 GNU/Linux

$ lsb_release -a
LSB Version:    :base-4.0-amd64:base-4.0-noarch:core-4.0-amd64:core-4.0-noarch:graphics-4.0-amd64:graphics-4.0-noarch:printing-4.0-amd64:printing-4.0-noarch
Distributor ID: RedHatEnterpriseServer
Description:    Red Hat Enterprise Linux Server release 6.7 (Santiago)
Release:        6.7
Codename:       Santiago

$ cat /etc/lsb-release
LSB_VERSION=base-4.0-amd64:base-4.0-noarch:core-4.0-amd64:core-4.0-noarch:graphics-4.0-amd64:graphics-4.0-noarch:printing-4.0-amd64:printing-4.0-noarch

$ cat /etc/issue.net
Red Hat Enterprise Linux Server release 6.7 (Santiago)
Kernel \r on an \m

$ cat /etc/debian_version
cat: /etc/debian_version: No such file or directory
```
2. Java Beans Open Source Server, Enterprise Application Platform, Version 6
([JBOSS EAP 6](http://developers.redhat.com/products/eap/download/ "Download"))
3. Tomcat 7.0.70 Servlet/JSP Container
([Tomcat 7](https://tomcat.apache.org/tomcat-7.0-doc/index.html "Information"))
4. PostgreSQL 9.2 Database Engine
([PostgreSQL](https://www.postgresql.org/about/ "About"))

To get all of this to work together, we used a pre-defined OpenShift configuration with two
plugins we needed (Tomcat + PostgreSQL). We could have chosen any of _Java, Ruby, Node.js, Python,
PHP, etc_, but we decided to go with Java on this one. You can find more information about
OpenShift [here](https://www.openshift.com/features/ "Features") and even register for free.
OpenShift has a free configuration that allows you to run your server code in a slimmed-down
container, and you could pay to get more - but the free configuration was enough for the demo.

Getting started
---------------

We use IntelliJ IDEA, and suggest you use the same
([from here](https://www.jetbrains.com/idea/ "Download")). You can use other tools, but this
one is tested so it will make the build process much smoother.

**Loading the project code**

To load the server project, you need to open it from your IDE. Clone the repository to your disk,
click _"Open"_ in your IDE, and go to `Server` subdirectory. You should be able to open it by
selecting `pom.xml` configuration file. When everything loads, your IDE should start downloading
dependencies and when that's finished, you're almost half way there.

**Preparing Tomcat**

Your server code needs to run somewhere. We used Tomcat because it's convenient and easy to set
up, and it works well on `localhost` so you can test everything prior to deploying to an actual
remote server. When Tomcat is downloaded, unpack it and place it somewhere on the disk where it
will be easily accessible later. Go to your IDE and configure it to run the project on the
Tomcat installation you just unpacked. Here is how to do it for IntelliJ IDEA:
[How to run on Tomcat](https://www.jetbrains.com/help/idea/2016.2/defining-application-servers-in-intellij-idea.html)

**Preparing PostgreSQL**

You need to store data in the database, data such as... well, Timecrypt messages. We chose
PostgreSQL for the same reasons we chose everything else - it's stable, easy to use and enough
configurable for our needs. And of course, it runs well on `localhost`. When you downloaded
PostgreSQL, you'll be prompted to pick a "data directory" which should usually be something
you will know how to go back to later. We set these things usually to `Documents/PostgreSQL/data`
for all platforms. Set your password to something you will remember, if prompted to type one.
When you finished installing, you will need to set up the database so it knows what Timecrypt
messages are.

To set up your database, you need to open the PostgreSQL Admin program (usually called `pgAdmin3`)
and create a new database called `timecrypt`, lower case 't'. Actually you could change all of
this, but let's just keep it as-is for now - more information coming later.

The database preparation code (table creation, encryption functions, structures, etc) is placed
under `src/main/resources/postgresql`, so you can just copy/paste that into the SQL query panel
in `pgAdmin3` program, and run it. After a few milliseconds, it should finish with a positive
status message. You are now ready to run and use the Timecrypt server.

**Starting PostgreSQL**

If you can't connect to your PostgreSQL database and getting the notorious "Server not listening"
error message, you may need to start your database engine manually. To do this for the local
server, go to the "data directory" you specified during the PostgreSQL installation, and run this:
```bash
pg_ctl -o "-p 5432" start -w -D .
```
It should work on all platforms. It sets the database port to `5432`, but you can set it to
anything you want (as long as the port is free). The last dot in the end tells the command to
use the "current" folder as "data directory", so you will have to put in the _full path_ if you
are not running this command from your data directory.

Another way to use PostgreSQL is to use port forwarding. For example, to forward to OpenShift's
PostgreSQL database, you can use the following command
```bash
rhc port-forward timecrypt --service postgresql
```
We assume that your OpenShift "app name" is `timecrypt`, change that if necessary. This command
only forwards PostgreSQL, but it could forward other RHC services too, check
[these answers](http://stackoverflow.com/questions/20960407/openshift-how-to-connect-to-postgresql-from-my-pc)
for more information. Output of this command will let you know how to connect to the database.

**Email Setup**

Timecrypt can send invites to people who are meant to read your messages, and notifications to you
if you want to know when your messages have been read. To do this, we used an external email
service - Mailgun. They also have a free subscription with a limited number of messages, but
again, the free model was enough for the demo. Read more about Mailgun on
[the official website](https://mailgun.com "Mailgun") and on the
[OpenShift blog](https://blog.openshift.com/email-in-the-cloud-with-mailgun/ "Blog page").

To use Mailgun for sending emails, you will need to create an account and set it up to get an
API key which is used to authenticate your app before every request. When you get an API key,
it should be placed in your `local.properties` or in an environment variable - more on
configuration is written below. You can change your email service if you want, but this one is
tested and will work smoothly with other stuff.

To create fancy email templates scalable to mobile devices, you can use the
[BeeFree app](https://beefree.io/ "Template creator"), it works pretty well and has a nice
_drag'n'drop_ editor.

Code structure
--------------

This project is built and packaged using Maven (see the `pom.xml` file for all dependencies).
The code is packaged under the `v2` source folder, because 2.0 is the only currently available
version of the API. Next versions should be placed under `v3`, `v4`, etc.

If you want to use the same code, but change some routines or the whole layer implementation,
you can do that in several ways:

* Change/add `local.properties` configuration located in `src/main/webapp/` to point to your
database, your app server, your email provider - but keep the current logic. This configuration
is currently not used on the demo server, but it is used when building locally. These properties
tell the server which database address to use, as well as which port, username, and password. It
also contains the Mailgun API key, or you could put your other configuration parameters in here.
You can see the parameter names we used in `database/posgtgresql/PosgresConfig.java`.

* Change/add environment variables for your machine or user account to point to your database,
your app server, your email provider - but keep the current logic. If `local.properties` are
not available, the default PostgreSQL implementation will look for environment variables next.
You can see the variable names we used in `database/posgtgresql/PosgresConfig.java`.

* Change the data storage engine completely. To do this, you will need to implement the
`database/TimecryptDataStore` interface on your class, located under a different package in the
`database` package. Also, you need to implement a provider technique (choose caching method,
creation method, destruction method, etc), see example at
`database/postgresql/PostgresProvider.java`. Instead of using our default provider, you will
have to use your own in `servlets/TimecryptApiServlet.java`.

* Change how your email works. You can do this either in `email/EmailConfig.java` to change
the parameter configuration, or in `email/EmailSender.java` to change the behavior. There are
also two email templates in `src/main/webapp/` for two email message types Timecrypt can send.

* Change whether the API servlets allow _HTTP GET_ and/or _HTTP POST_. You can do that by
overriding methods `servlets/TimecryptApiServlet#isPostAllowed()` and
`servlets/TimecryptApiServlet#isGetAllowed()`.

* Write the server from ground up. Although possible, we discourage this because you may deviate
from the Timecrypt API that all Timecrypt-capable servers conform to, described on the
repository root.