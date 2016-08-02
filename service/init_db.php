<?php

	define("DEBUG", "false");

	$db_hostname = $DEBUG ? "localhost" : "singlenote.db.10181513.hostedresource.com";
	$db_database = $DEBUG ? "single_note" : "singlenote";
	$db_username = $DEBUG ? "root" : "singlenote";
	$db_password = $DEBUG ? "" : "Auto1050!";

	// initialize database
	$mySql = new mysqli($db_hostname, $db_username, $db_password, $db_database);

	// check connection
	if (mysqli_connect_errno()) {
		// printf("Connect failed: %s\n", mysqli_connect_error());
		// exit();
		die("-1");
	}

//	$mySql->query("CREATE TABLE myCity LIKE City");

//	$query = "INSERT INTO myCity VALUES (NULL, 'Stuttgart', 'DEU', 'Stuttgart', 617000)";
//	$mySql->query($query);

//	printf ("New Record has id %d.\n", $mySql->insert_id);

	// drop table
//	$mySql->query("DROP TABLE myCity");

	// close connection
	// $mySql->close();

?>