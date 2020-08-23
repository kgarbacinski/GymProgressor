<?php 
	$codesArray = array();

	$handle = fopen("codes.txt", "r");
	if ($handle) {
		while (($line = fgets($handle)) != false) {
			// process the line read.
			$line = str_replace("\r\n","",$line);
			array_push($codesArray, $line);
		}

		fclose($handle);
	} else {
		// error opening the file.
	} 
	
	$conn = mysqli_connect("localhost", "root", '', "users");
	
	foreach($codesArray as $code){
		$sql = "INSERT INTO access_codes_table (Value) VALUES ('$code');";
		mysqli_query($conn, $sql);
	}
	
?>