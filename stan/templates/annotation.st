annotation(breadcrumb, notifications, header) ::= <<
	$header()$
	$breadcrumbs(breadcrumb)$
	
	<ul class="nav nav-tabs ">
  		<li role="presentation" class="active"><a href="#">Annotation</a></li>
  		<li role="presentation"><a href="/ontologies">Ontologies</a></li>
  		<li role="presentation"><a href="/set-namespace">Namespace</a></li>
  		<div class="pull-right">
	  		<a type="button" class="btn btn-primary" id="save" name="save">Save</a>
	  		<div class="btn-group">
			  <a type="button" id="export" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    <span id="export-label">Export</span> <span class="caret"></span>
			  </a>
			  <ul class="dropdown-menu">
			    <li><a name="export-mappings" id="export-mappings">Export mappings</a></li>
			    <li><a name="export-triples" id="export-triples">Export triples</a></li>
			  </ul>
			</div>
  		</div>
	</ul>
	<br>

	$alerts(notifications)$
	
	<div id="alert-message">
	</div>
	
	<table data-classes="table table-hover table-condensed" 
		   data-height="600" 
		   data-striped="true" 
		   data-toggle="table"
		   data-pagination="true"
		   data-side-pagination="server"
		   data-page-size="100"
		   data-url="/annotation/rows"
		   id="table">
	    <thead>
			<tr>
				$header:{ h |
	    			<th data-field="$i$">
	    				<a id="header-$i$" name="header-$i$" object="$h.object$" property-type="$h.propertyType$" class="$h.styleClass$" onclick="prepareModal('header-$i$')" data-toggle="modal" data-target="#annotationPopup" title="$h.uri$">$h.label$</a>
	    			</th>
	    		}$
    		</tr>
	    </thead>
	</table>
	
	<div class="modal fade" id="annotationPopup" tabindex="-1" role="dialog" aria-labelledby="modal-title">
  		<div class="modal-dialog modal-lg" role="document">
	    	<div class="modal-content">
	      		<div class="modal-header">
	        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        		<h3 class="modal-title" id="modal-title">Annotate column</h3>
	      		</div>
	      		
	      		<div class="modal-body">
				  <ul class="nav nav-tabs nav-justified" role="tablist">
				    <li role="presentation" class="active"><a href="#property" name="property" aria-controls="property" role="tab" data-toggle="tab">Annotate as a property</a></li>
				    <li role="presentation"><a href="#subject" name="subject" aria-controls="subject" role="tab" data-toggle="tab">Set as the subject column</a></li>
				  </ul>
				  <input type="hidden" id="column-id">
				  <input type="hidden" id="knowledgeBase" value="dbpedia">
				
				  <div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="property">
				    	<br>
				    	<form name="annotation-form">
				    		<div class="row">
							  <div class="col-xs-4 triple">
							  	<label class="control-label">Subject type</label>
							    <input type="text" class="form-control input-lg" id="subject-type" placeholder="Not already set" value="" disabled>
							  </div>
							  <div class="col-xs-4 triple">
							  	<label class="control-label">Property</label>
							  	<div class="input-group">
							  		<span class="input-group-btn">
							        	<a class="btn btn-lg disabled btn-default" id="property-icon-type">-</a>
						      		</span>
								  	<input type="text" autocomplete="off" id="annotation" name="annotation" class="form-control input-lg" placeholder="Your annotation" data-toggle="popover" data-placement="top" data-trigger="manual" data-content="Please insert a property">
								</div>
							  </div>
							  <div class="col-xs-4 triple">
							  	<label class="control-label">Object type</label>
							  	<input type="hidden" id="typeSet" value="datatype">
							    <input type="text" autocomplete="off" id="object-type" name="object-type" class="form-control input-lg">
							  </div>
							</div>
							<br>				    		
						 	
			        		<div class="panel panel-default panel-small">
							  	<div class="panel-heading">
							  		<strong>Suggestions</strong>
							  	</div>
							
								<div class="list-group" id="loading">
									<img src="/assets/img/loading.gif" class="loading" alt="loading">
									<p class="loading">STAN is matching your request against millions of triples...</p>
								</div>
								<div class="list-group" id="candidates" style="display: none;">
								</div>
							</div>
				      		<div class="modal-footer">
				        		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				        		<button type="button" name="save-annotation" onclick="annotate()" class="btn btn-primary">Save</button>
				      		</div>
		   		  		</form>
				    </div>
				    
				    <div role="tabpanel" class="tab-pane" id="subject">
				    	<br>
				    	<form name="set-subject-form">
				    		<div class="checkbox">
							    <label>
							      <input type="checkbox" name="isSubject" id="isSubject"> Check if the column is the subject
							    </label>
					  		</div>
					  		<input class="form-control" id="type" name="type" type="text" autocomplete="off" placeholder="Choose a type if the column is the subject..." disabled data-toggle="popover" data-placement="top" data-trigger="manual" data-content="Please insert a type">
					    	<div class="modal-footer">
				        		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				        		<button type="button" name="set-subject" onclick="setSubject()" class="btn btn-primary">Save</button>
				      		</div>
			      		</form>
				    </div>
				    
				  </div>
				
				</div>
				
	      		
	    	</div>
	  	</div>
	</div>
	
	$footer()$
>>