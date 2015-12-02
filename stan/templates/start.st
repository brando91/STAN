start(notifications, breadcrumb) ::= <<
	$header()$
	$breadcrumbs(breadcrumb)$
	
	<ul class="nav nav-tabs ">
  		<li role="presentation" class="active"><a href="#from-file" id="fromFile">Upload from file</a></li>
  		<li role="presentation"><a href="#from-url" id="fromUrl">Upload from URL</a></li>
	</ul>
	<br>
	
	$alerts(notifications)$
	
	<div class="tab-content">
	    <div role="tabpanel" class="tab-pane active" id="from-file">
	    	<div class="jumbotron">
				<h2>Upload your CSV table</h2>
				<br>
		
				<form name="upload-table" action="/upload-table" method="post" enctype="multipart/form-data" class="form-horizontal">
					<div class="fileinput fileinput-new input-group" data-provides="fileinput">
				  		<div class="form-control" data-trigger="fileinput">
				  			<i class="glyphicon glyphicon-file fileinput-exists"></i> 
				  			<span class="fileinput-filename"></span>
			  			</div>
					  	<span class="input-group-addon btn btn-primary btn-file primary">
					  		<span class="fileinput-new">
					  			Select file
				  			</span>
				  			<span class="fileinput-exists">
				  				Change
			  				</span>
			  				<input type="file" name="table" id="table" required>
						</span>
					  	<a href="#" class="input-group-addon btn btn-primary fileinput-exists primary" data-dismiss="fileinput">
					  		Remove
				  		</a>
					</div>
					
					<p>
						<small>Specify a separator character, eventually a text delimiter and if the table has an header:</small>
					</p>
					<div class="form-group">
					    <label for="separator" class="col-sm-2 control-label">Separator</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="separator" placeholder="column1,column2,column3" name="separator" required>
					    </div>
					  
					    <label for="delimiter" class="col-sm-2 control-label">Text Delimiter</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="delimiter" placeholder='"value","value, containing, the, separator"' name="delimiter">
					    </div>
				  	</div>
				  	
				  	<div class="form-group">
					    <div class="col-sm-offset-2 col-sm-10">
					      <div class="checkbox">
					        <label>
					          <input type="checkbox" name="header"> <b>Has header</b>
					        </label>
					      </div>
					    </div>
				  	</div>
				  	
				  	<br>
					<button type="submit" id="upload" name="upload" onclick="loading()" class="btn btn-primary">Upload</button>
				</form>
		
			</div>
		</div>
		
	    <div role="tabpanel" class="tab-pane" id="from-url">
	    	<div class="jumbotron">
				<h2>Upload a table from URL</h2>
				<br>
		
				<form name="upload-table-url" action="/upload-table-url" method="post" enctype="multipart/form-data" class="form-horizontal">
					<div class="input-group url">
					  <span class="input-group-addon" id="basic-addon1"><span class="glyphicon glyphicon-link" aria-hidden="true"></span></span>
					  <input type="text" name="url" id="url" class="form-control" placeholder="The table URL" aria-describedby="basic-addon1" required>
					</div>
					
					<p>
						<small>Specify a separator character, eventually a text delimiter and if the table has an header:</small>
					</p>
					<div class="form-group">
					    <label for="separator" class="col-sm-2 control-label">Separator</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="separator-url" placeholder="column1,column2,column3" name="separator" required>
					    </div>
					  
					    <label for="delimiter" class="col-sm-2 control-label">Text Delimiter</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="delimiter-url" placeholder='"value","value, containing, the, separator"' name="delimiter">
					    </div>
				  	</div>
				  	
				  	<div class="form-group">
					    <div class="col-sm-offset-2 col-sm-10">
					      <div class="checkbox">
					        <label>
					          <input type="checkbox" name="header"> <b>Has header</b>
					        </label>
					      </div>
					    </div>
				  	</div>
				  	
				  	<br>
					<button type="submit" id="upload-url" name="upload" onclick="loadingUrl()" class="btn btn-primary">Upload</button>
				</form>
		
			</div>
	    </div>
	</div>
  
	
	$footer()$
>>