<?php
	function DuplicateLoginName($LogName){

		//$myfile = fopen("members.txt", "r") or die("Unable to open file!");

		$document = file_get_contents("members.txt");
		$lines = explode("\n", $document);

		foreach ($lines as $newline) {
			$tokens = explode("|", $newline);

			if(strcmp($LogName, $tokens[4]) === 0){
				return false;
			}
		}
		return true;
	}
?>