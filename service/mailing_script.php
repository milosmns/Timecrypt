<?php

	/**
	 * Mailing Script <br>
	 * Author: Milos Marinkovic <a href="milosmns@gmail.com">milosmns@gmail.com</a> <br>
	 * Version: 1.6 <br><br>
	 *
	 * Sends an email to the specified address. It can be <br>
	 * written in HTML. Must be URL-encoded, and use POST method.
	 * <br><br>
	 *
	 * Params <br>
	 *      String to           - Email to send the message to <br>
	 *      String fromMail     - Email to set as the sender <br>
	 *      String fromName     - Name to set as the sender <br>
	 *      String subject      - Subject of the Email <br>
	 *      String replyToMail  - Email to send the reply to <br>
	 *      String replyToName  - Name to send the reply to <br>
	 *      String text         - Message text, may be HTML <br>
	 *      String authCode     - Code to authorize the sending <br><br>
	 *
	 * Output <br>
	 * 		Prints 1 if successful, -1 otherwise.
	 */

	function isEmpty($var) {
		if (empty($var) || $var == null || is_null($var)) {
			return true;
		}
		return false;
	}

	function getUrlVar($name) {
		if (isset($_POST[$name])) {
			if (!isEmpty($_POST[$name])) {
				$nameVar = trim(stripslashes(urldecode($_POST[$name])));
				if (!empty($nameVar)) {
					return $nameVar;
				}
			}
		}

		return null;
	}

	function isAnyElementNull($arr) {
		if (is_array($arr))
			foreach ($arr as $elem)
				if ($elem == null)
					return true;

		return false;
	}

	function sendEmail($to, $subject, $fromMail, $fromName, $replyToMail, $replyToName, $text) {
		$headers   = array();
		$headers[] = "MIME-Version: 1.0";
		$headers[] = "Content-type: text/html; charset=utf-8";
		$headers[] = "From: $fromName <$fromMail>";
		$headers[] = "Reply-To: $replyToName <$replyToMail>";
		$headers[] = "Subject: {$subject}";
		$headers[] = "X-Mailer: PHP/".phpversion();

		return mail($to, $subject, $text, implode("\r\n", $headers));
	}


	/* ************************** SCRIPT START ************************** */

	$to = getUrlVar("to");
	$fromMail = getUrlVar("fromMail");
	$fromName = getUrlVar("fromName");
	$subject = getUrlVar("subject");
	$replyToMail = getUrlVar("replyToMail");
	$replyToName = getUrlVar("replyToName");
	$text = getUrlVar("text");
	$authCode = getUrlVar("authCode");

	// manually add auth codes here
	// 5689124498 -> timecrypt.co
	// 9167586643 -> vaptim.com | secret

	$authCodes = array("5689124498", "9167586643", "5469887913", "8233120569", "9236158888");

	if (! isAnyElementNull(array ($authCode, $to, $fromMail, $fromName, $subject, $replyToMail, $replyToName, $text)) ) {

		if (in_array($authCode, $authCodes)) {

			$sent = sendEmail($to, $subject, $fromMail, $fromName, $replyToMail, $replyToName, $text);

			if ($sent) {
				echo "1";
			} else {
				echo "-1";
			}

		} else {
			echo "-1";
		}

	} else {
		echo "-1";
	}

?>