// Copyright 2012 cyber communications inc.
// Date: Tue Oct  1 11:10:14 JST 2019

var callback_kruxcall=function(o){window.SURFPOINT=o;window.bk_async=function(){bk_ignore_outside_iframe=false;var arr=new Array();try{arr.push(['response_cat_gname',category_group_name]);}catch(e){}
try{arr.push(['response_cat_name',category_name]);}catch(e){}
try{arr.push(['response_savetitle',savetitle]);}catch(e){}
try{arr.push(['response_ip_bcflag',SURFPOINT.BCFlag]);}catch(e){}
try{arr.push(['response_ip_office',SURFPOINT.OrgOfficeCode]);}catch(e){}
try{arr.push(['response_ip_indipendent',SURFPOINT.OrgIndependentCode]);}catch(e){}
try{arr.push(['response_ip_ipo',SURFPOINT.OrgIpoType]);}catch(e){}
try{arr.push(['response_ip_cap',SURFPOINT.OrgCapitalCode]);}catch(e){}
try{arr.push(['response_ip_emp',SURFPOINT.OrgEmployeesCode]);}catch(e){}
try{arr.push(['response_ip_gross',SURFPOINT.OrgGrossCode]);}catch(e){}
try{arr.push(['response_ip_indcat_l',SURFPOINT.OrgIndustrialCategoryL]);}catch(e){}
try{arr.push(['response_ip_indcat_m',SURFPOINT.OrgIndustrialCategoryM]);}catch(e){}
try{arr.push(['response_ip_indcat_s',SURFPOINT.OrgIndustrialCategoryS]);}catch(e){}
try{arr.push(['response_ip_indcat_t',SURFPOINT.OrgIndustrialCategoryT]);}catch(e){}
try{arr.push(['response_ip_pref',SURFPOINT.PrefAName]);}catch(e){}
try{arr.push(['response_ip_org',SURFPOINT.OrgName]);}catch(e){}
try{arr.push(['predix_url',document.location.href]);}catch(e){}
try{if(1<document.location.search.length){window.pxqry=document.location.search.substring(1);}else{window.pxqry="";}
arr.push(['predix_qry',pxqry]);}catch(e){}
function makeTag(args){if(args.length==2){if((typeof(args[0])==='string'&&args[0]!=='')&&(typeof(args[1])!=='undefined'&&args[1]!==null&&args[1]!=='')){bk_addPageCtx(args[0],args[1]);}}}
for(var i=0;i<arr.length;i++){makeTag(arr[i]);}
bk_addPageCtx('editcheck','20171129_1');BKTAG.doTag(44862,1);};(function(){var scripts=document.getElementsByTagName('script')[0];var s=document.createElement('script');s.async=true;s.src="https://tags.bkrtx.com/js/bk-coretag.js";scripts.parentNode.insertBefore(s,scripts);}());};callback_kruxcall(new Array());(function(){var axel=Math.random()+"";var a=axel*10000000000000;document.write('<iframe src="https://5157214.fls.doubleclick.net/activityi;src=5157214;type=rtiiy0;cat=rtm6s0;dc_lat=;dc_rdid=;tag_for_child_directed_treatment=;ord='+a+'?" width="1" height="1" frameborder="0" style="display:none"></iframe>');})();