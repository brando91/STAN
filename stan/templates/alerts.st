alerts(notifications) ::= <<
	$notifications.errors:{ e |
		<div class="alert alert-danger alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  $e$
		</div>
  	}$
  	
  	$notifications.infos:{ info |
		<div class="alert alert-success alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  $info$
		</div>
  	}$
>>