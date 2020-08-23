<?php
	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		$idJSON = $_POST['user_id'];
		$id = (int)(json_decode($idJSON, true));
		
		$gender = null;
		$age = null;
		$weight = null;
		
		if(isset($_POST['gender'])) $gender = $_POST['gender'];
		if(isset($_POST['age'])) $age = $_POST['age'];
		if(isset($_POST['weight'])) $weight = $_POST['weight'];
		
		require_once 'connect.php';
		
		$sql = "UPDATE userinfo_table SET ";

		if($gender != null) $sql .= " gender = '$gender',";
		if($age != null) $sql .= " age = '$age',";
		if($weight != null) $sql .= " weight = '$weight',";
		
		$sql = trim($sql, ',');
		$sql .= " WHERE UserId = '$id'";

		$response = mysqli_query($conn, $sql);
		
		mysqli_close($conn);
	}
?>