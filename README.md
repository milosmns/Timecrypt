Project Timecrypt
=================
What is Timecrypt?
------------------
Timecrypt is an _open-source_, non-realtime communication API that can be used to create, store, send and read 
encrypted messages. All messages created through Timecrypt can have:
* **Password** -- Lock your message using a special passphrase, also needed to unlock the message
* **Self-destruct date** -- For example, request to destroy the message in 5 days
* **Invitation email address** -- Send a direct invitation to the message
* **Title** -- Give a short insight on what the message contains
* **Notification email address** -- Receive a notification when the message is read
* **Allowed views** -- For example, request to destroy the message after it has been seen 3 times

For a quick demonstration, you can use [the project website](http://www.timecrypt.co "Timecrypt Project"). 
Server and client code on the demo page are in sync with this repository.

API Specification
-----------------
**General information**

Timecrypt is a **RESTful** API. All calls to a Timecrypt-capable server need to be prefixed with the API version, meaning 
that, for example, if you want to use API version 2.0 (which is the minimum) to create a new message, you need to target
`/v2/create`. All data sent should be passed as simple, separate HTML request parameters. No authentication is required.

Responses from any Timecrypt-capable server are properly structured JSON strings. There is a parameter in every JSON 
response called `status_code` (integer) that tells you whether the operation finished successfully or not. Status code 
will always be set to zero (0) when the operation completes successfully, and set to various negative numbers when it does
not. Different API calls have different error codes (check the reference below), but all errors will always be negative.

To give it a test run, you can use the demo server at [this location](http://timecrypt-angrybyte.rhcloud.com/ "Demo").

**Create message**

To create a message, you need to call the `/create` endpoint. Parameters for the `/create` endpoint are:
* `text` -- Contents of the message, **must not** be empty
* `views` -- How many times is this message allowed to be shown, can be empty, defaults to 1
* `lifetime` -- When to self-destruct the message (format: `yyyy-MM-dd`), must be at least one day in the future, 
can be empty, defaults to _tomorrow_
* `email_to` -- Where to send the invitation to this message, can be empty, no default
* `email_from` -- Where to send the "message read" notification, can be empty, no default
* `title` -- Title of the message, can be empty, no default
* `password` -- Which passphrase to use to encrypt the message, can be empty, defaults to datastore's passphrase

Error codes held in the `status_code` field from the `/create` endpoint response can be:
* `-1` -- Internal error
* `-4` -- Missing text parameter
* `-5` -- Title length is out of bounds
* `-6` -- Text length is out of bounds
* `-7` -- View count is out of bounds
* `-8` -- Password not usable
* `-9` -- Invalid destruct date or out of bounds
* `-10` -- Invitation email is not valid
* `-11` -- Notification email is not valid

A basic *HTTP POST* example would be to load `[demo-server]/v2/create` with `text` parameter set to `Hello world`. This 
will create a new message and return a JSON object containing a unique message ID. Successfull API calls to `/create`
endpoint will always result in a valid JSON response containing the message ID and a mandatory status code set to zero:
```json
{
    "id": "f78a89", 
    "status_code": 0
}
```

**Read message**

When your message is created and you have the unique message ID, your meessage can be loaded using the `read` endpoint.
Parameters for the `/read` endpoint are:
* `id` -- A unique message identifier obtained through `create` call, must not be empty
* `password` -- A passphrase to use for decryption, can be empty, defaults to datastore's passphrase
 
Error codes held in the `status_code` field from the `/read` endpoint response can be:
* `-1` -- Internal error
* `-2` -- Missing message ID
* `-3` -- Invalid message ID

A basic _HTTP POST_ example would be to load `[demo-server]/v2/read` with `id` parameter set to whatever you got from
`/create` endpoint as the message ID. As password is optional, decryption could fail - resulting in indecipherable values
for all text properties of the requested message. Successfull API calls to `/read` endopint will always result in a 
valid JSON response with status code field set to zero, and any `null` properties completely left out.

Here is a sample JSON for a full response (message had no `null` properties):
```json
{
    "text": "Here is the message",
    "title": "Custom title",
    "views": 2,
    "lifetime": "2016-08-15",
    "status_code": 0
}
```

Note that using `/read` endpoint with a valid message ID reduces the `views` value by one for that message. If allowed
views are depleted (value is zero), the message will be completely removed from the datastore. To check if a message
exists, or to check if it needs to be unlocked using a user password, you can use the `/is-locked` endpoint.

**Message lock check**

When your message is created and you have a unique message ID, your message may have a special passphrase/password that
is required to decipher it. You will most likely need to check whether your message has a password or not, and then
prompt the user to enter one accordingly. To check whether your message has a special passphrase, you can use the 
`/is-locked` endpoint. Parameters for the `/is-locked` endpoint are:
* `id` -- A unique message identifier obtained through `create` call, must not be empty

Error codes held in the `status_code` field from the `/is-locked` endpoint response can be:
* `-1` -- Internal error
* `-2` -- Missing message ID
* `-3` -- Invalid message ID

A basic _HTTP POST_ example would be to load `[demo-server]/v2/is-locked` with `id` parameter set to whatever you got from
`/create` endpoint as the message ID. Successfull API calls to `/is-locked` endopint will always result in a valid JSON
response with status code field set to zero and `locked` property set to either `true` or `false`, depending on whether
the message has a special passphrase. Here is an example of a lock check response:

```json
{
    "locked": true,
    "status_code": 0
}
```

Frequently asked questions
--------------------------
-- _If it is open-source, can everyone see my messages?_  
No. Timecrypt is now open-source because being transparent is the only way to really achieve privacy. 
Timecrypt API is structured in a way that allows app developers to easily swap modules such as security layer, 
encryption layer, database engine, business logic and control flows, and easily integrate their privately hosted 
Timecrypt-capable services into their existing infrastructures. This means that you can run _your own_ Timecrypt
server, on your own network, free of charge.

-- _There is some code in the repository, can we just use that?_  
All of the code contained in this repository serves a **demo** purpose. Yes, you _could_ use that code, and if you
want to learn more about how to use it, go to the `Server` directory and read the doc. 

-- _Why does the given code only serve a demo purpose?_  
Above everything else, Timecrypt is an API. This means that data structures, parameter names, and even server responses 
**need to be the same** across all platforms and all custom service implementations. To be able to communicate with a 
Timecrypt-capable server, the server needs to conform to these standards. To market this approach a bit more.. this will
allow people to use their Timecrypt clients with **any** Timecrypt-capable server, or even switch while using the app. 
Imagine that.. Timecrypt API is licensed under Apache 2.0 License, and we believe that this specific license protects 
the API well enough. If you understand this concern separation, the code given in this repository is fine, as long as you
are not looking for something super-cryptic, super-secure and super-speedy.

-- _We do not want the user interface, is that required by the API or the License?_  
No. The user interface at [the demo page](http://timecrypt.co "Timecrypt Project") is inspired by some of the Mission 
Impossible scenes and communication UI. You can read more about that if you go to the `WebClient` directory. You are
not forced to use that UI, but it is easily configurable to use your server and app pages instead of the demo ones.

Support
-------
If you found an error while using the API, please 
[file an issue](https://github.com/milosmns/Timecrypt/issues/new).
All patches are allowed (but will most likely be heavily discussed prior to applying), and may be submitted by 
[forking this project](https://github.com/milosmns/Timecrypt/fork) and
submitting a pull request through GitHub.

Some more help can be found here:
* StackOverflow: [here](http://stackoverflow.com/questions/tagged/timecrypt) or 
[here](http://stackoverflow.com/questions/tagged/timecrypt)
* On my [blog](http://angrybyte.me)

This repository was built by merging two remote repositories, where _Server_ directory is one, and _WebClient_ directory 
is the other. Since I used [OpenShift](https://www.openshift.com/index.html "OpenShift") to run the server code, and
[BitBucket](https://www.atlassian.com/software/bitbucket "BitBucket") to run the web client code, I did the
[following procedure](https://www.kernel.org/pub/software/scm/git/docs/howto/using-merge-subtree.html "Merge repos") to
merge the repositories as subtrees:
```bash
echo "Adding repository as remote"
git remote add -f openshift-server ssh://.../timecrypt.git
echo "Starting merge"
git merge -s ours --no-commit openshift-server/master
echo "Loading remote files into subdirectory"
git read-tree --prefix=Server/ -u openshift-server/master
echo "Finishing merge"
git commit -m "Merging remote project as subdirectory"
```

And the same thing for the other remote called `web-client` from the `WebClient` subdirectory.  
Whenever that code changes, this repository will have both subtrees updated using:
```bash
echo "Updating subtrees"
git pull -s subtree openshift-server master
git pull -s subtree web-client master
```

Any help on building an Android or an iOS app is appreciated. Also any Slack enthusiasts wanting Timecrypt integration
are welcome to join :)














