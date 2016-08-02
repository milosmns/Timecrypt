<?php

	/**
	 * Message getting script <br>
	 * Author: Milos Marinkovic <a href="mailto:milosmns@gmail.com">milosmns@gmail.com</a> <br>
	 * Version: 1.0 <br><br>
	 *
	 * Message getting service script, gets info about the message with given message ID. <br><br>
	 *
	 * Required params <br>
	 * 		id (String [1-40]) - Message ID <br>
	 * Optional params <br>
	 * 		password (String [20]) - If you want to decrypt the message <br>
	 *		language (String [2]) - Currently supported languages: { 'en', 'sr' } <br>
	 *
	 * Output <br>
	 *		1) Non-existing message ID (deleted, invalid or wrong): -1
	 *      2) Existing message ID: Message text, title, lifetime and remaining views, JSON. <br><br>
	 *
	 * Example <br>
	 *      { "text" : "This is my text!", "views" : "2", "title" : "My Message", "lifetime" : "2015-02-14" }
	 */


	/* ******************************** Document start ******************************** */

	include ("core.php");

	$id = getUrlVar("id"); // required, min 1 char
	$password = getUrlVar("password"); // 20 chars, used to encrypt and decrypt
	$language = getUrlVar("language"); // send "Message read" email in this language

	if ($id != null) {
		if ($password == null || isEmpty($password))
			$password = NULL;

		if ($language == null || !validateLang($language))
			$language = "en";

		include ("init_db.php"); // generates the mySql object

		// deleting old messages before the reading, because the requested message can be too old
		// deleting viewed messages too (viewed_times < 1)
		if (deleteOldAndSeenNotes($mySql)) {
			$intId = intval($id, 36);

			$escaped = $mySql->escape_string($intId);
			$sql = "SELECT * FROM `note` WHERE `note`.`id` = '".$escaped."'";
			$result = $mySql->query($sql);

			if ($result) {
				$row = $result->fetch_assoc();

				if ($row) {
					// free the result set
					$result->free();

					$views = $row["view_times"];
					$lifetime = $row["lifetime"];
					$email = $row["email"];
					$text_enc = $row["text_encrypted"];
					$title = $row["title"];

					$sql = "UPDATE `note` SET `note`.`view_times` = '".(--$views)."' WHERE `note`.`id` = '".$escaped."'";
					$result = $mySql->query($sql);

					if ($result) {
						$text_dec = decrypt($text_enc, $password);

						$text = getConfirmationEmailText($language, $id, $views);
						$subject = getSubjectForEmail($language, "read");
						$sentFrom = "notifications@timecrypt.co";
						$sentFromName = "TimeCrypt";
						$replyTo = "no-reply@timecrypt.co";
						$replyToName = "Do Not Reply";
						$authCode = "5689124498";

						// no matter if it's not sent, this is not too important
						$sentEmail = sendEmailUsingScript($email, $sentFrom, $sentFromName, $subject, $replyTo, $replyToName, $text, $authCode);

						// example: { "text" : "This is my text!", "views" : "2", "title" : "My Message", "lifetime" : "2015-02-14" }
						$output = array (
							"text" => $text_dec,
							"views" => (string) $views,
							"title" => $title,
							"lifetime" => $lifetime
						);

						$json = json_encode($output);

						if (!isEmpty($json)) {
							// print the JSON in UTF-8
							header ('Content-Type: text/html; charset=UTF-8');
							echo $json;
						} else {
							echo "-1";
						}

					} else {
						echo "-1";
					}

				} else {
					echo "-1";
				}

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