Connect PostgreSQL locally

http://stackoverflow.com/questions/20960407/openshift-how-to-connect-to-postgresql-from-my-pc
rhc port-forward timecrypt --service postgresql
pg_ctl -o "-p 5432" start -w -D .
// https://blog.openshift.com/email-in-the-cloud-with-mailgun/
// https://beefree.io/

change possible in several layers:

* config info (local.properties in webapp/)
* data store choice (nosql, sql, etc)
* data store implementation (implement interface)
* data store provider (override behavior)
* post/get switch in servlets (override methods)