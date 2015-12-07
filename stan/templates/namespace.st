namespace(breadcrumb, notifications, tableName, content) ::= <<
	$header()$
	$breadcrumbs(breadcrumb)$
	
	<ul class="nav nav-tabs ">
  		<li role="presentation"><a href="/annotation">Annotation</a></li>
  		<li role="presentation"><a href="/ontologies">Ontologies</a></li>
  		<li role="presentation" class="active"><a href="#">Namespace</a></li>
	</ul>
	<br>

	$alerts(notifications)$
	
	<div class="row-fluid">
		<div class="col-md-12">
	  		<h4>Set the namespace for the entities of your table $tableName$</h4>
	  		<br>
	  		
	  		<form name="set-namespace" action="set-namespace/save" method="post">
	  			<div class="form-group">
	  				<input type="text" class="form-control input-lg" name="namespace" placeholder="http://yournamespace/" value="$content$" required>
	  			</div>
	  			
	  			<button type="submit" name="set" class="btn btn-primary">Save</button>
	  		</form>
  		</div>
	</div>
	
	$footer()$
>>