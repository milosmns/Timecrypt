<?php

	/**
	 * Password checking script <br>
	 * Author: Milos Marinkovic <a href="mailto:milosmns@gmail.com">milosmns@gmail.com</a> <br>
	 * Version: 1.0 <br><br>
	 *
	 * Gets password presence for the given message ID. <br><br>
	 *
	 * Required params <br>
	 * 		id (String [1-40]) - Message ID <br>
	 *
	 * Output <br>
	 *		1) Non-existing message ID (deleted, invalid or wrong): -1
	 * 		2) Existing message ID, no password: 0
	 *		3) Existing message ID, password: 1
	 */


	/* ******************************** Document start ******************************** */

	include ("core.php");

	$id = getUrlVar("id"); // required, min 1 char

	if ($id != null) {
		include ("init_db.php"); // generates the mySql object

		$intId = intval($id, 36);

		$escaped = $mySql->escape_string($intId);
		$sql = "SELECT * FROM `note` WHERE `note`.`id` = '".$escaped."'";
		$result = $mySql->query($sql);

		if ($result) {
			$row = $result->fetch_assoc();

			if ($row) {
				// free the result set
				$result->free();
				$locked = $row["locked"];
				echo $locked;
			} else {
				echo "-1";
			}

		} else {
			echo "-1";
		}

		include ("close_db.php");

	} else {
		echo "-1";
	}

?>