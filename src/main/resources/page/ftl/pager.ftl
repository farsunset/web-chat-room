<#macro pager page>
<span style="float: left;margin-left: 12px;padding-top: 5px;">
<@spring.messageArgs  "module.global.pager.description",[page.totalElements?c,page.totalPages]  />
</span>

<#if page.totalPages gt 1>
			<ul class="pagination" style="margin: 10px 0;padding-top: 1px;">
			<#if page.first>
			  <li  class="disabled"><a>&laquo;</a></li>
			<#else>
			  <li style='cursor: pointer;'><a onclick='gotoPage(0)'>&laquo;</a></li>
			  <li style='cursor: pointer;'><a onclick="gotoPage(${page.number-1})"><@spring.message 'module.global.pager.previous'/></a></li>
			</#if>
			
			<#list page.number - 5..page.number+6 as i>
			  <#if i gte 0 && i lte page.totalPages -1>
			     <#if i == page.number>
			        <li class="active"><a >${i+1}</a></li>
			     <#else>
			        <li style='cursor: pointer;'><a onclick='gotoPage(${i})'>${i+1}</a></li>
			     </#if>
			  </#if>
			</#list>
			 
			<#if page.last>
				<li class="disabled"><a>&raquo;</a></li>
			<#else>
			    <li style='cursor: pointer;'><a onclick="gotoPage(${page.number+1})"><@spring.message 'module.global.pager.next'/></a></li>
			    <li style='cursor: pointer;'><a onclick="gotoPage(${page.totalPages-1})">&raquo;</a></li>
			</#if>
			<li><input   type="number" id='GoPageNumber' style='padding:6px;width: 60px;height: 34px;' class="input-pagenumber form-control" ><a style='float: right;cursor: pointer;' onclick="gotoPage($('#GoPageNumber').val()-1)">GO</a></li>
		</ul>
</#if>
</#macro>
