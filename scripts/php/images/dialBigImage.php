<?php
$inputs = explode(',',$_GET['s']);
$score = $inputs[0];

$image_width=1040;$image_height=612;
$img = imagecreatetruecolor($image_width,$image_height); //make image variable
$maintext = imagecolorallocate($img,64,64,64);
$secondarytext = imagecolorallocate($img,101,112,119);
$white = imagecolorallocate($img,255,255,255);
$black = imagecolorallocate($img,0,0,0);
$grey = imagecolorallocate($img,232,235,241);
$red = imagecolorallocate($img,230,79,34);
$yellow = imagecolorallocate($img,253,184,19);
$orange = imagecolorallocate($img,255,165,0);
$magenta = imagecolorallocate($img,255,0,255);
$violet = imagecolorallocate($img,238,130,238);
$blue = imagecolorallocate($img,34,156,188);
$green = imagecolorallocate($img,122,156,57);

// white background
imagefilledrectangle($img,0,0,$image_width,$image_height,$white);

$colors = array(
  $red,
  $yellow,
  $orange,
  $magenta,
  $violet,
  $blue
);

$scoreTheta = ($score/100)*180;
$scoreX = (cos(deg2rad(180 - $scoreTheta)) * 510) + 520;
$scoreY = 520 - (sin(deg2rad(180 - $scoreTheta)) * 510); 

$cutoffs = array();
$currWidth = 0;

for( $i = 1; $i < count($inputs); $i++ ) {
  $cutoff = ($inputs[$i]/100)*180;
  $width = $cutoff - $currWidth;
  $cutoffs[$i-1] = array(
    "label" => ''.$inputs[$i],
    "width" => $width,
    "labelX" => (cos(deg2rad(180 - $cutoff)) * 284) + 500,
    "labelY" => 520 - (sin(deg2rad(180 - $cutoff)) * 284),
    "color" => $colors[ $i - 1 ]
  );
  $currWidth = $cutoff;
}

$position = 180;

for( $j = 0; $j < count($cutoffs); $j++ ) {
  imagefilledarc($img,520,520,1040,1040,$position,$position+$cutoffs[$j]["width"]-1,$cutoffs[$j]["color"],IMG_ARC_PIE);
  imagefilledarc($img,520,520,1040,1040,$position+$cutoffs[$j]["width"],$position+$cutoffs[$j]["width"]+1,$white,IMG_ARC_PIE);
  $position = $position+$cutoffs[$j]["width"]+1;
}

// The green arc
imagefilledarc($img,520,520,1040,1040,$position,360,$green,IMG_ARC_PIE);

// The "hollow" center
imagefilledarc($img,520,520,680,680,180,360,$white,IMG_ARC_PIE);

$values = array(
            468, 428,  // Point 1 (x, y)
            572, 428, // Point 2 (x, y)
            628, 520,  // Point 3 (x, y)
            572, 612,  // Point 4 (x, y)
            468, 612,  // Point 5 (x, y)
            412, 520    // Point 6 (x, y)
            );

// The honeycomb
imagefilledpolygon($img, $values, 6, $maintext);

// The numbers
imagefttext($img, 26, 0, 184, 520, $secondarytext, './HelveticaBold.ttf', '0');
imagefttext($img, 26, 0, 800, 520, $secondarytext, './HelveticaBold.ttf', '100');

for( $y = 0; $y < count($cutoffs); $y++ ) {
  imagefttext($img, 26, 0, $cutoffs[$y]["labelX"], $cutoffs[$y]["labelY"], $secondarytext, './HelveticaBold.ttf', $cutoffs[$y]["label"]);
}

// The pointer
imagefilledarc($img,$scoreX,$scoreY,1020,1020,$scoreTheta-3,$scoreTheta+3,$maintext,IMG_ARC_PIE);

// The score
imagefttext($img, 72, 0, 470, 560, $white, './HelveticaBold.ttf', $score);

header('Content-Type: image/png');
imagepng($img);
imagedestroy($img);
?>

