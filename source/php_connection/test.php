<?php
require_once 'connect.php';


for($i=45;$i<=119;$i++){	
	if($i >= 45  && $i <= 49){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.61, 0.99, 1.5, 2.12, 2.8);";
	}
	
	if($i >= 50 && $i <= 54){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.61, 0.98, 1.46, 2.03, 2.67);";
	}
	
	if($i >= 55 && $i <= 59){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ($i, 0.62, 0.97, 1.42, 1.95, 2.54);";
	}
	
	if($i >= 60 && $i <= 64){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.62, 0.95, 1.37, 1.88, 2.44);";
	}
	
	if($i >= 65 && $i <= 69){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i',  0.61, 0.93, 1.34, 1.81, 2.34);";
	}
	
	if($i >= 70 && $i <= 74){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.61, 0.92, 1.3, 1.75, 2.25);";
	}
	
	if($i >= 75 && $i <= 79){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.61, 0.9, 1.27, 1.7, 2.17);";
	}
	
	if($i >= 80 && $i <= 84){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.6, 0.88, 1.23, 1.65, 2.09);";
	}
	
	if($i >= 85 && $i <= 89){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.6, 0.87, 1.2, 1.6, 2.03);";
	}
	
	if($i >= 90 && $i <= 94){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.59, 0.85, 1.18, 1.55, 1.96);";
	}
	
	if($i >= 95 && $i <= 99){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.58, 0.84, 1.15, 1.51, 1.91);";
	}
	
	if($i >= 100 && $i <= 104){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.58, 0.82, 1.12, 1.47, 1.85);";
	}
	
	if($i >= 105 && $i <= 109){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.57, 0.81, 1.1, 1.44, 1.8);";
	}
	
	if($i >= 110 && $i <= 114){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.56, 0.79, 1.08, 1.4, 1.75);";
	}
	
	if($i >= 115 && $i <= 119){ 
		$sql = "INSERT INTO female_deadlift_standards VALUES ('$i', 0.56, 0.78, 1.06, 1.37, 1.71);";
	}
	
	mysqli_query($conn, $sql);

	}
?>	