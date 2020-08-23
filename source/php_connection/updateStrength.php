<?php 
	$idJSON = $_POST['user_id'];
	$id = (int)(json_decode($idJSON, true));
	
	$strengthJSON = $_POST['strength_map'];
	
	require_once 'connect.php';
	
	$sql = "UPDATE measurements_table SET StrengthJSON = '$strengthJSON' WHERE UserId = '$id';";
	
	mysqli_query($conn, $sql);
	mysqli_close($conn);
	

?>