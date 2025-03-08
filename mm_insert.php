<?php

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "mm_grocerydb";

$conn = new mysqli($servername, $username, $password, $dbname);

$response = array();

if (isset($_POST['mm_itm_name']) && isset($_POST['mm_itm_price']) && isset($_POST['mm_itm_qty'])) {
    $mm_itm_name = $_POST['mm_itm_name'];
    $mm_itm_price = $_POST['mm_itm_price'];
    $mm_itm_qty = $_POST['mm_itm_qty'];
    $mm_id = isset($_POST['mm_id']) ? $_POST['mm_id'] : "";

    // Check if mm_id is not empty and is a valid integer (if editing)
    if (!empty($mm_id) && is_numeric($mm_id)) {
        // If mm_id is provided and valid, update the record
        $stmt = $conn->prepare("UPDATE `mm_tblitems` SET `mm_itm_name` = ?, `mm_itm_price` = ?, `mm_itm_qty` = ? WHERE `mm_id` = ?");
        $stmt->bind_param("sdii", $mm_itm_name, $mm_itm_price, $mm_itm_qty, $mm_id);
    } else {
        // If mm_id is empty or invalid, perform an insert
        $stmt = $conn->prepare("INSERT INTO `mm_tblitems`(`mm_itm_name`, `mm_itm_price`, `mm_itm_qty`) VALUES (?, ?, ?)");
        $stmt->bind_param("sdi", $mm_itm_name, $mm_itm_price, $mm_itm_qty);
    }

    if ($stmt->execute()) {
        $response['error'] = false;
        $response['message'] = "Success!";
    } else {
        $response['error'] = true;
        $response['message'] = "Failed: " . $conn->error;
    }
} else {
    $response['error'] = true;
    $response['message'] = "Insufficient parameters";
}

$conn->close();
echo json_encode($response);

?>
