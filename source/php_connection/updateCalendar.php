<?php
	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		$idJSON = $_POST['user_id'];
		$noteJSON = $_POST['note_list'];
		$timeJSON = $_POST['time_list'];
		

		$id = (int)(json_decode($idJSON, true));
		
		//$noteArray = json_decode($noteJSON, true);
		//$timeArray = json_decode($timeJSON, true);
		
		require_once 'connect.php';
		
		$sql = "DELETE FROM calendar_table WHERE UserId='$id';";
		
		mysqli_query($conn, $sql);
		
		
		foreach(array_combine($timeArray, $noteArray) as $time => $note){
			$sql = "INSERT INTO calendar_table (TimeId, UserId, Note) VALUES ('$time', '$id', '$note');";
			mysqli_query($conn, $sql);

		}
		mysqli_close($conn);
	}
		
	

?>