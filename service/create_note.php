<?php

	/**
	 * Message creating script <br>
	 * Author: Milos Marinkovic <a href="mailto:milosmns@gmail.com">milosmns@gmail.com</a> <br>
	 * Version: 1.0 <br><br>
	 *
	 * Message creating service page. Allows user to create a message in the TimeCrypt app. <br><br>
	 * 
	 * Required params <br>
	 * 		text (String [1-500]) - Message text <br>
	 * Optional params <br>
	 * 		views (Integer [1-100]) - How many times it can be viewed. Default: 1 <br>
	 * 		password (String [20]) - If you want to encrypt the message <br>
	 * 		title (String [30]) - Set a title to the message <br>
	 * 		lifetime (Date) - When does the message auto-delete. Format: YYYY-MM-DD <br>
	 *		language (String [2]) - Currently supported languages: { 'en', 'sr' } <br>
	 * Extra params <br>
	 *		email_to (String [50]) - Send the link to someone's email <br>
	 *		email_from (String [50]) - Send the notification when the message is read <br><br>
	 * 
	 * Output <br>
	 *		1) Something goes wrong: -1 <br>
	 *		2) Everything goes well: URL to the message <br>
	 */


	/* ******************************** Document start ******************************** */

	include("core.php");

	$hashId = ""; // required for DB, 128 chars
	$text = getUrlVar("text"); // required for DB, 500 chars, 2000 chars in DB, min 1 char
	$views = getUrlVar("views"); // required for DB, [1..100]
	$password = getUrlVar("password"); // 20 chars, used to encrypt and decrypt
	$title = getUrlVar("title"); // up to 30 chars
	$lifetime = getUrlVar("lifetime"); // SQL Date
	$emailFrom = getUrlVar("email_from"); // send an email there when the message is read
	$emailTo = getUrlVar("email_to"); // send an email there when the message is read
	$language = getUrlVar("language"); // generate URL in this language and send Email in this lang

	$defaultLifetime = "2016-01-01";

	if ($text != null) {
		$hasPassword = !!($password != null && strlen(trim($password)) > 0);
		$encrypted = encrypt($text, $password);

		if ( ((int) intval($views)) < 1 )
			$views = 1;
		else
			$views = (int) intval($views);

		if ($title == null || isEmpty($title))
			$title = NULL;

		if ($lifetime == null || !validateDate($lifetime))
			$lifetime = $defaultLifetime;

		if ($emailFrom == null || !filter_var($emailFrom, FILTER_VALIDATE_EMAIL))
			$emailFrom = NULL;

		if ($emailTo == null || !filter_var($emailTo, FILTER_VALIDATE_EMAIL))
			$emailTo = NULL;

		if ($language == null || !validateLang($language))
			$language = "en";

		include("init_db.php"); // generates the mySql object

		$sql = "INSERT INTO `note` VALUES (".
					$mySql->escape_string($views).", ".
					"NULL".", ".
					($lifetime == NULL ? "NULL" : ("'".$mySql->escape_string($lifetime)."'")).", ".
					($emailFrom == NULL ? "NULL" : ("'".$mySql->escape_string($emailFrom)."'")).", ".
					"'".$mySql->escape_string($encrypted)."'".", ".
					($title == NULL ? "NULL" : ("'".$mySql->escape_string($title)."'")).", ".
					($hasPassword ? "1" : "0").
				")";

		$result = $mySql->query($sql);

		if ($result) {

			$newRowId = $mySql->insert_id;
			$hashRowId = base_convert($newRowId, 10, 36);

			if ($language == "en")
				$urlBase = "http://timecrypt.co/?c=";
			else if ($language == "sr")
				$urlBase = "http://timecrypt.co/".$language."/?c=";
			else
				$urlBase = "http://timecrypt.co/";

			$url = $urlBase.urlencode($hashRowId);

			$text = getEmailTextBase($language).'<a href="'.$url.'">'.$url.'</a><br><br><i>TimeCrypt</i>';

			$subject = getSubjectForEmail($language, "url");
			$sentFrom = "text@timecrypt.co";
			$sentFromName = "TimeCrypt";
			$replyTo = "no-reply@timecrypt.co";
			$replyToName = "Do Not Reply";
			$authCode = "5689124498";

			if ($emailTo != NULL)
				$sentEmail = sendEmailUsingScript($emailTo, $sentFrom, $sentFromName, $subject, $replyTo, $replyToName, $text, $authCode);
			else
				$sentEmail = true;

			if ($sentEmail) {
				header ('Content-Type: text/html; charset=UTF-8');
				echo $url;
			} else {
				echo "-1";
			}

		} else {
			echo "-1";
		}

		include("close_db.php");

	} else {
		echo "-1";
	}

?>