<?php 
	$idJSON = $_POST['user_id'];
	$id = (int)(json_decode($idJSON, true));
	
	$bodyJSON = $_POST['body_list'];
	
	require_once 'connect.php';
	
	$sql = "UPDATE measurements_table SET BodyJSON = '$bodyJSON' WHERE UserId = '$id';";
	
	mysqli_query($conn, $sql);
	mysqli_close($conn);
	

?>