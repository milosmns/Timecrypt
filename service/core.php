<?php

	function isEmpty($var) {
		if (empty($var) || $var == null || is_null($var)) {
			return true;
		}
		return false;
	}

	// TODO change to POST
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

	function convertPassword($password) {
		if (isEmpty($password))
			$passwordVar = "crypt9812text3467";
		else
			$passwordVar = trim($password);

		return $passwordVar;
	}

	function convertText($text) {
		if (isEmpty($text))
			$textVar = "[ ? ]";
		else
			$textVar = trim($text);

		return $textVar;
	}

	function getIndexForPercent($len, $percent) {
		if ($percent <= 0)
			$percentVar = 1;
		else if ($percent > 100)
			$percentVar = 100;
		else
			$percentVar = (int) $percent;

		$num = (int) ($percentVar / 100 * $len - 1);
		if ($num < 0)
			$num = 0;
		else if ($num > ($len - 1))
			$num = ($len - 1);

		return $num;
	}

	function encrypt($text, $password) {
		$textVar = convertText($text);
		$key = convertPassword($password);
		$len = strlen($key);

		// in theory, these can all be the same value
		// get ASCII values for some characters from the password, 8 times smaller
		$numbers = array();
		$numbers[] = ord ($key[ getIndexForPercent($len,   0) ]); // first char value
		$numbers[] = ord ($key[ getIndexForPercent($len,  30) ]); // char val at 30%
		$numbers[] = ord ($key[ getIndexForPercent($len,  55) ]); // char val at 55%
		$numbers[] = ord ($key[ getIndexForPercent($len,  70) ]); // char val at 70%
		$numbers[] = ord ($key[ getIndexForPercent($len, 100) ]); // last char value

		for ($i = 0; $i < count($numbers); $i++) {
			$numbers[$i] = (int) ($numbers[$i] / 8);
		}

		$numbersI = 0;
		$textI = 0;

		$encryptedArr = array();
		do {
			$newCharVal = ord($textVar[$textI]) + $numbers[$numbersI];

			// add char to encrypted text array
			$encryptedArr[] = chr($newCharVal);

			// reset the number counter to 0, this way it loops through numbers
			if ($numbersI == (count($numbers) - 1))
				$numbersI = 0;
			else
				$numbersI++;
			// increase $textI
			$textI++;

		} while ($textI < strlen($textVar));

		// generate String from array
		$encryptedString = implode('', $encryptedArr);
		return $encryptedString;
	}

	function decrypt($text, $password) {
		$textVar = convertText($text);
		$key = convertPassword($password);
		$len = strlen($key);

		// in theory, these can all be the same value
		// get ASCII values for some characters from the password, 8 times smaller
		$numbers = array();
		$numbers[] = ord ($key[ getIndexForPercent($len,   0) ]); // first char value
		$numbers[] = ord ($key[ getIndexForPercent($len,  30) ]); // char val at 30%
		$numbers[] = ord ($key[ getIndexForPercent($len,  55) ]); // char val at 55%
		$numbers[] = ord ($key[ getIndexForPercent($len,  70) ]); // char val at 70%
		$numbers[] = ord ($key[ getIndexForPercent($len, 100) ]); // last char value

		for ($i = 0; $i < count($numbers); $i++) {
			$numbers[$i] = (int) ($numbers[$i] / 8);
		}

		$numbersI = 0;
		$textI = 0;

		$decryptedArr = array();
		do {
			$newCharVal = ord($textVar[$textI]) - $numbers[$numbersI];

			// first visible char is TAB at index 9
			if ($newCharVal < 9)
				$newCharVal = 9;

			// add char to decrypted text array
			$decryptedArr[] = chr($newCharVal);

			// reset the number counter to 0, this way it loops through numbers
			if ($numbersI == (count($numbers) - 1))
				$numbersI = 0;
			else
				$numbersI++;
			// increase $textI
			$textI++;

		} while ($textI < strlen($textVar));

		// generate String from array
		$decryptedString = implode('', $decryptedArr);
		return $decryptedString;
	}

	/**
	 * Validates the date string provided. Format: YYYY-MM-DD
	 * @param $var String date
	 * @return bool True if date is valid, false otherwise
	 */
	function validateDate($var) {
		if (isEmpty($var))
			return false;

		$varTrm = trim($var);
		$arr = explode("-", $varTrm);

		if (count($arr) != 3)
			return false;

		if (!checkdate (
			(int) intval($arr[1]),
			(int) intval($arr[2]),
			(int) intval($arr[0])
		)) return false;

		// all good
		return true;
	}

	function validateLang($lang) {
		if (isEmpty($lang))
			return false;

		$langVar = trim($lang);
		if (strcasecmp($langVar, "en") || strcasecmp($langVar, "sr"))
			return true;

		return false;
	}

	// type should be [url] or [read]
	function getSubjectForEmail($lang, $type) {
		if (isEmpty($lang) || isEmpty($type))
			return "";

		$langVar = trim($lang);
		$typeVar = trim($type);

		if ($langVar == "en" && $type == "url") {
			return "Your message is waiting!";
		} else if ($langVar == "sr" && $type == "url") {
			return "Tvoja poruka čeka!";
		} else if ($langVar == "en" && $type == "read") {
			return "Your message was read!";
		} else if ($langVar == "sr" && $type == "read") {
			return "Tvoja poruka je pročitana!";
		} else {
			return "";
		}
	}

	function getEmailTextBase($lang) {
		if (isEmpty($lang))
			return "";

		$langVar = trim($lang);
		if ($langVar == "en") {
			return "Dear future visitor,<br><br>Your message is waiting! When you click ".
					"on this link, you will have a chance to see the message, but be careful! ".
					"If the sender has set the message view limit to one, you will be able to see ".
					"it only once! If you need a password, and you type in the wrong one, you will ".
					"get the message all garbled up. Thanks for using our service, and good luck!<br><br>";
		} else if ($langVar == "sr") {
			return "Budući korisniče,<br><br>Tvoja poruka čeka! Ako klikneš na link koji se nalazi u ovoj ".
					"poruci, imaćeš priliku da vidiš poruka, ali oprezno! Ako je pošiljalac postavio ".
					"ograničenje za pregled poruka na jedan, moći ćeš da vidiš poruku samo jednom! ".
					"Ako ti treba lozinka, a ukucaš pogrešnu, dobićeš poruka u zbrljanom formatu, i ".
					"nećeš moći da je pročitaš. Hvala što koristiš naš servis, i srećno!<br><br>";
		} else {
			return "";
		}
	}

	function getConfirmationEmailText($lang, $noteId, $views) {
		if (isEmpty($lang))
			return "";

		$langVar = trim($lang);
		if ($langVar == "en") {
			return "Dear user,<br><br>Your message [<b>$noteId</b>] has been read! You should probably ".
					"confirm with your reader that he/she actually read the message. This message has ".
					"<b>$views</b> more view(s) left.<br>Thanks for using our service!<br><br>".
					"<i>TimeCrypt</i>";
		} else if ($langVar == "sr") {
			return "Dragi korisniče,<br><br>Tvoja poruka [<b>$noteId</b>] je pročitana! Najbolje je da ".
					"potvrdiš sa osobom kojoj je bila poslata da je pročitana kako treba. Ova poruka ima još ".
					"<b>$views</b> preostalih čitanja.<br>Hvala što koristiš naš servis!<br><br>".
					"<i>TimeCrypt</i>";
		} else {
			return "";
		}
	}

	function sendEmailUsingScript($to, $fromMail, $fromName, $subject, $replyToMail, $replyToName, $text, $authCode) {
		$url = 'http://cryptext.netai.net/mailing_script.php';
		$params = array (
			'to'            => $to,
			'fromMail'      => $fromMail,
			'fromName'      => $fromName,
			'subject'       => $subject,
			'replyToMail'   => $replyToMail,
			'replyToName'   => $replyToName,
			'text'          => $text,
			'authCode'      => $authCode
		);

		// use key 'http' even if you send the request to https://
		$options = array (
			'http' => array (
				'header' => "Content-type: application/x-www-form-urlencoded\r\n",
				'method' => 'POST',
				'content' => http_build_query($params),
			),
		);

		$context = stream_context_create($options);
		$result = file_get_contents($url, false, $context);

		if (isEmpty($result) || trim($result) != 1)
			return false;

		return true;
	}

	function deleteOldAndSeenNotes(mysqli $mySql) {
		$sql = "DELETE FROM `note` WHERE date(`note`.`lifetime`) < date(curdate()) OR `note`.`view_times` < '1'";
		$result = $mySql->query($sql);

		if ($result) {
			return true;
		}

		return false;
	}

?>