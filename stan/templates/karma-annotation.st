karma-annotation(notifications, predictions, breadcrumb) ::= <<
	$header()$	
	$breadcrumbs(breadcrumb)$
	$alerts(notifications)$
	
	<div class="panel panel-default">
	  	<div class="panel-heading">
	  		<strong>Predictions</strong>
	  	</div>
	
		<ul class="list-group">
			$predictions:{ prediction |
		  		<li class="list-group-item">
		    		<span class="badge progress-bar-info">$prediction.score$</span>
		    			$prediction.label$
		  		</li>
		  	}$
		</ul>
	</div>

	$footer()$
>>