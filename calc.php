<?php

    function exp_hash_norm($exp){
        $exps = [-2 => "001", -1 => "010", 0 => "011", 1 => "100", 2 => "101", 3 => "110"];

	return $exps[$exp];
    }

    function exp_hash_Nnorm($exp){
        $exps = [-2 => "001", -1 => "010", 0 => "011", 1 => "100", 2 => "101", 3 => "110"];

        return array_search($exp,$exps);
    }

    function NNcalcFP($binary){
	$a = 2 + 2;
	    
        return $a;
    }

    function calcFP($binary,$number){
	$converted = [];
	$binary = str_split($binary);


	if($number > 0){
            array_push($converted,0);
	}
	else{
            array_push($converted,1);
	}
	if($number > 1){
            $exp = (array_search(',',$binary) - 1);
	}
	else{
            $exp = (-(array_search(',',$binary)) + array_search(',',$binary));
	}
	array_push($converted,exp_hash_norm($exp));

	$n = count($binary);
	$counter = 0;
	for($i = 1; $i < $n; $i++){
            if($counter == 4){
                break;
	    }
	    if(strcmp($binary[$i], ',') != 0){
                $counter++;
	        array_push($converted,$binary[$i]);
	    }
	}
	while($counter < 4){
            array_push($converted,0);
	    $counter++;
	    
	}
	return implode($converted);
    }

    function calcFracPart($fraction){
        $frac_bin = [];
	$mult_times = 0;
	$pos = 0;
	while($mult_times < 7){
            $fraction = $fraction * 2;
	    if($fraction >= 1){
                $frac_bin[$pos] = 1;
		$pos++;
		$fraction--;
	    }
	    else{
                $frac_bin[$pos] = 0;
		$pos++;
	    }

	    if($fraction == 0){
                break;
	    }
	    $mult_times++;
	}
	return implode($frac_bin);
    }

    function convertFP($numToConvert){
	$num_copy = $numToConvert;

	if($numToConvert < 0){
            $num_copy = -$numToConvert;
	}

	$num_dec_part = (int) $num_copy;
	$num_frac_part = $num_copy - $num_dec_part;
	$bin_dec_part = decbin($num_dec_part);
	$bin_frac_part = calcFracPart($num_frac_part);
        $num_bin_full = implode(array($bin_dec_part,',', $bin_frac_part));
         
	//return implode(array($bin_dec_part,',',$bin_frac_part));
	if($num_copy < 0.25){
            $num_converted = NNcalcFP($num_bin_full,$numToConvert);
	}
	else{
            $num_converted = calcFP($num_bin_full,$numToConvert);
	}
	//return implode(array($bin_dec_part,',', $bin_frac_part));
	return $num_converted;
    }

    require("Template.class.php");
    $tpl = new Template("index.html");
    $value = $_POST["FPnum"];
    if($value != 0){	    
        $tpl->RESULTADO = convertFP($value);
	$tpl->block("BLOCK_RESULTADO");
    }
    $tpl -> show();
?>
