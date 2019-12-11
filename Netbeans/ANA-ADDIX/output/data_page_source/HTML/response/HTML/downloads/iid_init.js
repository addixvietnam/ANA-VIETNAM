({
	name:'adtlgcen_Cookie',
	set:function(n,v,c){var d,e='';d=new Date();if(c){d.setTime(d.getTime()+c*60*60*1000);};e='; expires='+d.toGMTString();document.cookie=escape(n)+'='+v+e+'; path=/';},
	get:function(n){var e,b,p,c=document.cookie;p=n+'=';b=c.indexOf(';'+' '+p);if(b==-1){b=c.indexOf(p);if(b!==0){return '';}}else{b+=2;}e=c.indexOf(';',b);if(e==-1){e=c.length;}return unescape(c.substring(b+p.length,e));},
	unset:function(n){return this.set(n,'');},
	init:function(){window[this.name]=this;}
}).init();

var cX = cX || {};
cX.callQueue = cX.callQueue || [];

window.en_ads =  window.en_ads || [];

if (!window.admp_) {

	var CUSTOMER_ID = '0066';

	//Seems to be a Safari bug. All properties of location are undefined
	try { window.loc_ = (window.location.href == 'undefined' && JSON && JSON.parse && JSON.stringify) ? JSON.parse(JSON.stringify(window.location)) : window.location; }catch(err){window.loc_ = window.location;}
	if (!window.loc_.origin) {
		window.loc_.origin = window.loc_.protocol + '//' + window.loc_.hostname + (window.loc_.port ? ':' + window.loc_.port: '');
	}

	var adtlgcen_SETTINGS = {
		TC_URL: window.loc_.protocol + '//admp-tc-iid.adtlgc.com',
		minPercOfVisibility: 50,//minimum percent of visibility of AD in-view tracking
		maxDwellTimeToSend: 60, //maximum total value (in seconds) reported for single AD for in-view time
		ADSERVER_ID: '0',
		SCRIPT_VERSION_PARAMETER: '&v=2.37',
		AR_ENABLED: true,//if Audience Radar is enabled
		PL_ENABLED: false,//if placements tracking is enabled
		CINT_URL: 'https://collector.cint.com?a=2495&i='+(parseInt(CUSTOMER_ID))+'&id=evid_'+CUSTOMER_ID+':',
		DFP_NETWORK_ID: '',
		DFP_NETWORK_ALIAS: ''
	};

	var enr_vars = {
		evid: 'evid_'+CUSTOMER_ID,
		evid_v: 'evid_v_'+CUSTOMER_ID,
		adptseg: 'adptseg_'+CUSTOMER_ID,
		adptset: 'adptset_'+CUSTOMER_ID,
		adptcmp: 'adptcmp_'+CUSTOMER_ID,
		adptcmp_ref: 'adptcmp_ref_'+CUSTOMER_ID,
		cintsent: 'enr_cint_sent_'+CUSTOMER_ID,
		cmpmap: 'cmpmap_'+CUSTOMER_ID,
		cmpmap_ref: 'cmpmap_ref_'+CUSTOMER_ID,
		inview_data_container: 'en_data',
		targeting_var: 'enreachresp',
		segm_targeting_var: 'ecsegm',
		segmArr_targeting_var: 'ecsegmArr',
		dataRequest: 'adapt_dataRequest_'+CUSTOMER_ID+'_admp'
	};

	var enr_cx_SETTINGS = {
    	syncEnabled: true,
    	persistedUpdateQueryId: 'ac0cb7579090343e146e0264f09c1f26029bc2bb',
    	cxPrefixes: ['eii']
    };

    var segmentGroups = {
        'kv1001': '-ecsegm-gender',
        'kv1002': '-ecsegm-age',
        'kv1003': '-ecsegm-education',
        'kv1005': '-ecsegm-occupation',
        'kv1006': '-ecsegm-childrencount',
        'kv1007': '-ecsegm-householdsize',
        'kv1013': '-ecsegm-marital-status'
    };

    var segmentValues = {
        'kv1001=a': 'Male',
        'kv1001=b': 'Female',
        'kv1002=a': 'Under 18',
        'kv1002=b': '18-24',
        'kv1002=c': '25-34',
        'kv1002=d': '35-44',
        'kv1002=e': '45-54',
        'kv1002=f': '55-64',
        'kv1002=g': 'Over 65',
        'kv1005=d': 'Studies',
        'kv1005=e': 'Executive',
        'kv1005=f': 'Homemaker',
        'kv1005=g': 'Office worker',
        'kv1005=h': 'Own business/Self-employed/Freelance',
        'kv1005=i': 'Part-time worker',
        'kv1005=j': 'Public official/Government worker',
        'kv1005=k': 'Unemployed',
        'kv1005=l': 'Other',
        'kv1006=a': '1',
        'kv1006=b': '2',
        'kv1006=c': '3 or more',
        'kv1006=d': 'None',
        'kv1007=a': '1',
        'kv1007=b': '2-4',
        'kv1007=c': '5 or more',
        'kv1013=a': 'Married',
        'kv1013=b': 'Not married'
    };

	window.en_slots = window.en_slots || {};
	window.adtlgcen = window.adtlgcen || {};
	adtlgcen.config = adtlgcen.config || {};
	adtlgcen.util = adtlgcen.util || {};
	adtlgcen.event = adtlgcen.event || {};

    adtlgcen.config.tagsToMonitor = {
        'A': '',
        'EMBED': '',
        'OBJECT': '',
        'IFRAME': '',
        'IMG': '',
    	'AREA': '',
        'CANVAS': '',
        'SVG': ''
    };

	adtlgcen.config.blocksToTrack = adtlgcen.config.blocksToTrack || [];
	adtlgcen.config.bannersToTrack = adtlgcen.config.bannersToTrack || [];
	adtlgcen.config.eventsCache = adtlgcen.config.eventsCache || [];
    adtlgcen.config.placements = adtlgcen.config.placements || [];
    adtlgcen.config.reported_placements = {};
	adtlgcen.config.suspendAdEvents = true;

	if (!Array.prototype.indexOf) {
	    Array.prototype.indexOf = function (searchElement /*, fromIndex */ ) {
	        'use strict';
	        if (this == null) {
	            throw new TypeError();
	        }
	        var t = Object(this);
	        var len = t.length >>> 0;
	        if (len === 0) {
	            return -1;
	        }
	        var n = 0;
	        if (arguments.length > 0) {
	            n = Number(arguments[1]);
	            if (n != n) { // shortcut for verifying if it's NaN
	                n = 0;
	            } else if (n != 0 && n != Infinity && n != -Infinity) {
	                n = (n > 0 || -1) * Math.floor(Math.abs(n));
	            }
	        }
	        if (n >= len) {
	            return -1;
	        }
	        var k = n >= 0 ? n : Math.max(len - Math.abs(n), 0);
	        for (; k < len; k++) {
	            if (k in t && t[k] === searchElement) {
	                return k;
	            }
	        }
	        return -1;
	    }
	}

    function BaseSize() {
        this.width = 0;
        this.height = 0;
        this.toString = function () {
            var result = '{';
            for (var i in this) {
                if (typeof this[i] != 'function') {
                    result += i + ': ' + this[i] + ', ';
                }
            }
            return result.substring(0, result.length - 2) + '}';
        };
    };

    function InViewNode(opt_options) {
        function init(elements, callback) {
            if (!(elements instanceof Array)) {
                elements = [elements];
            }
            for (var i = 0; i < elements.length; i++) {
                addElementToTrack(elements[i]);
            }
            callback_ = callback;
            run_();
        };
        this['init'] = init;

        function addElementToTrack(element) {
            if (typeof element == 'string') {
                element = doc_.getElementById(element);
            }
            if (element) {
                element[dataKey_] = new InViewData;
                elements_.push(element);
            }
        };
        this['addElementToTrack'] = addElementToTrack;

        function removeElementToTrack(elementToRemove) {
            if (typeof element == 'string') {
            	elementToRemove = doc_.getElementById(elementToRemove);
            }
            var newList = [];
            for (var i = 0; i < elements_.length; i++) {
            	var element = elements_[i];
            	if(element.id == elementToRemove.id){
            		elementToRemove[dataKey_] = new InViewData;
            	}else{
            		newList.push(element);
            	}
            }
            elements_ = newList;
        };
        this['removeElementToTrack'] = removeElementToTrack;

        function run_() {
            if (elements_ && (elements_.length > 0)) {
                var viewBox = getViewBox_();
                var root = getRoot_();
                for (var i = 0; i < elements_.length; i++) {
                    var element = elements_[i];
                    var nodeBox = getNodeBox_(element);
                    element[dataKey_].height = parseInt(getVisibleHeight_(viewBox, nodeBox) * 100 / nodeBox.height, 10);
                    element[dataKey_].width = parseInt(getVisibleWidth_(viewBox, nodeBox) * 100 / nodeBox.width, 10);
                    if (contains_(viewBox, nodeBox)) {
                        if (!element[dataKey_].start_) {
                            element[dataKey_].start_ = new Date().getTime();
                        }
                        element[dataKey_]['time'] = new Date().getTime() - (element[dataKey_].start_ || 0);
                        element[dataKey_]['visible'] = true;
                        callback_(element);
                    } else {
                        if (element[dataKey_].start_) {
                            element[dataKey_]['time'] = new Date().getTime() - element[dataKey_].start_;
                            element[dataKey_]['visible'] = false;
                            element[dataKey_].start_ = 0;
                            callback_(element);
                        }
                    }
                }
            }
            if (timer_) {
                win_.clearTimeout(timer_);
            }
            timer_ = win_.setTimeout(function(){run_();}, timeout_);
        };

        function contains_(viewBox, nodeBox) {
            return nodeBox.top + nodeBox.height > viewBox.top && nodeBox.top < viewBox.top + viewBox.height && nodeBox.left + nodeBox.width > viewBox.left && nodeBox.left < viewBox.left + viewBox.width;
        };

        function getVisibleHeight_(viewBox, nodeBox) {
            var result = viewBox.height;
            if (nodeBox.top - viewBox.top > 0) {
                result -= nodeBox.top - viewBox.top;
            }
            if (viewBox.top + viewBox.height - (nodeBox.top + nodeBox.height) > 0) {
                result -= viewBox.top + viewBox.height - (nodeBox.top + nodeBox.height);
            }
            if (result < 0) {
                result = 0;
            }
            return result;
        };

        function getVisibleWidth_(viewBox, nodeBox) {
            var result = viewBox.width;
            if (nodeBox.left - viewBox.left > 0) {
                result -= nodeBox.left - viewBox.left;
            }
            if (viewBox.left + viewBox.width - (nodeBox.left + nodeBox.width) > 0) {
                result -= viewBox.left + viewBox.width - (nodeBox.left + nodeBox.width);
            }
            if (result < 0) {
                result = 0;
            }
            return result;
        };

        function getRoot_() {
            if (!root_) {
                root_ = doc_.compatMode == 'CSS1Compat' ? doc_.documentElement : doc_.body;
            }
            return root_;
        };

        function getViewBox_() {
            var root = getRoot_();
            var box = new ClientRectImpl;
            box.height = win_.innerHeight || root.clientHeight || 0;
            box.width = win_.innerWidth || root.clientWidth || 0;
            box.top = win_.pageYOffset || root.scrollTop || 0;
            box.left = win_.pageXOffset || root.scrollLeft || 0;
            if (+'\v1') {
                if (root.scrollHeight) {
                    box.height -= getScrollBarSize_();
                }
                if (root.scrollWidth) {
                    box.width -= getScrollBarSize_();
                }
            }
            return box;
        }

        function getNodeBox_(element) {
            var root = getRoot_();
            var box = new ClientRectImpl;
            box.height = element.offsetHeight;
            box.width = element.offsetWidth;
            while (element.offsetParent) {
                box.top += element.offsetTop;
                box.left += element.offsetLeft;
                element = element.offsetParent;
            }
            return box;
        }

        function getScrollBarSize_() {
            if (!scrollBarSizeInitialized_) {
                var outer = doc_.createElement('P');
                outer.style.position = 'absolute';
                outer.style.top = '0px';
                outer.style.left = '0px';
                outer.style.visibility = 'hidden';
                outer.style.width = '200px';
                outer.style.height = '150px';
                outer.style.overflow = 'hidden';
                var inner = outer.appendChild(doc_.createElement('P'));
                inner.style.width = '100%';
                inner.style.height = '200px';
                doc_.body.appendChild(outer);
                var w1 = inner.offsetWidth;
                outer.style.overflow = 'scroll';
                var w2 = inner.offsetWidth;
                if (w1 == w2) {
                    w2 = outer.clientWidth;
                }
                doc_.body.removeChild(outer);
                scrollBarSize_ = w1 - w2;
                scrollBarSizeInitialized_ = 1;
            }
            return scrollBarSize_;
        }
        var win_ = window;
        var doc_ = win_.document;
        var root_ = null;
        var elements_ = [];
        var callback_ = null;
        var timer_ = 0;
        var scrollBarSize_ = 0;
        var scrollBarSizeInitialized_ = 0;
        var timeout_ = opt_options['timeout'] || 400;
        var dataKey_ = opt_options['key'] || 'en_data';
    };
    window['InViewNode'] = InViewNode;

    function InViewData() {
        BaseSize.call(this);
        this.time = 0;
        this.visible = false;
        this.iteration = 1;
        this['time'] = this.time;
        this['visible'] = this.visible;
        this.sentDwellTime = 0;
    }

    function ClientRectImpl() {
        BaseSize.call(this);
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
        this.left = 0;
    };

    function PlacementDetector(aliveTime) {
        function init() {
        	started_ = new Date().getTime();
            run_();
        }
        this['init'] = init;

        function finished(){
        	return !running;
        }
        this['finished'] = finished;

        function run_() {
        	running = 1;
        	adtlgcen.util.trackPlacements();

            for(var i=0; i < adtlgcen.config.blocksToTrack.length; i++){
            	adtlgcen.util.processCanvasAds(adtlgcen.config.blocksToTrack[i]);
            	adtlgcen.util.processIframes(adtlgcen.config.blocksToTrack[i]);
            }

            if (new Date().getTime() - started_ < aliveTime_) {
            	window.setTimeout(function(){
            		run_();
            	}, timeout_);
            }else{
            	running = 0;
            }
        }
        var timeout_ = 1000;
        var started_ = new Date().getTime();
        var running = 0;
        var aliveTime_ = aliveTime || 20 * 1000;
    };

    function DynamicLoadingDetector(aliveTime) {
        function init() {
        	started_ = new Date().getTime();
            run_();
        }
        this['init'] = init;

        function finished(){
        	return !running;
        }
        this['finished'] = finished;

        function run_() {
        	running = 1;
        	if(window['en_cur_loc']){
        		if(window.location.href != window['en_cur_loc']){
        			window['en_cur_loc'] = window.location.href;
        			try{
        				adtlgcen.config.reported_placements = {};
        				delete en_vp_pl_sent;
        				if(admp_.plDetector.finished()){
        		        	admp_.plDetector.init();
        		        }
        				adtlgcen.util.makePageImpCall();
        			}catch(e){
        				//ignore
        			}
        		}
        	}else{
        		window['en_cur_loc'] = window.location.href;
        	}

            window.setTimeout(function(){run_();}, timeout_);

        }
        var timeout_ = 200;
        var started_ = new Date().getTime();
        var running = 0;
    };

    window.en_regAD = function (contId) {
    	admp_.initTracking(contId);
	};

	adtlgcen.util.fib = function (n) {
        return n < 2 ? n : (adtlgcen.util.fib(n - 1) + adtlgcen.util.fib(n - 2));
    };
    adtlgcen.util.interval = function (n) {
        return n > 6 ? 10 : (adtlgcen.util.fib(n + 2) - 1);
    };

    adtlgcen.util.send = function (url, skipExtraparams, pixelImage) {
    	if(!skipExtraparams){
    		url += '&evid=' + window[enr_vars.evid];
    		if(window[enr_vars.evid_v]){
    			url += '&vv=' + window[enr_vars.evid_v];
    		}
    		url += adtlgcen_SETTINGS.SCRIPT_VERSION_PARAMETER;
    	}

        if(pixelImage){
			setTimeout(function(){
				var imgRequest = new Image(0, 0);
				imgRequest.src = url;
			},100);
			return;
		}

        if (navigator.appVersion.indexOf('MSIE') != -1 ) {
			var version = parseFloat(navigator.appVersion.split('MSIE')[1]);
		    if(version<8){
				setTimeout(function(){
					var request = new Image(0, 0);
					request.src = url;
				},1000);
				return;
		    }
	 	}

		var result = false;
		if (!result && typeof XDomainRequest!='undefined') {
			result = new XDomainRequest();
		}
		if (!result && typeof XMLHttpRequest!='undefined') {
			try {
				result = new XMLHttpRequest();
			} catch (e) {
				result=false;
			}
		}
	    if (!result && window.createRequest) {
	    	try {
	    		result = window.createRequest();
	    	} catch (e) {
	    		result=false;
	    	}
	    }
		if(result){
			result.open('GET', url,true);
			result.send('');
		}
		return result;
    };

    adtlgcen.util.composeStdParams = function (adid, bnid, cuid) {
    	var isRef = false;
        var campId = window[enr_vars.cmpmap] ? window[enr_vars.cmpmap][adid] : '';
        if (!campId) {
        	campId = window[enr_vars.cmpmap_ref] ? window[enr_vars.cmpmap_ref][adid] : '';
            if(!campId){
            	return;
            }
            isRef = true;
        }
        return '&adId=' + adid + '&bnId=' + bnid + '&pId=' + cuid + '&reference='+isRef+'&campaignId=' + campId + '&location=' + (encodeURIComponent || escape)(window.loc_.href) + '&cb=' + (new Date()).getTime();
    };

    adtlgcen.util.composeUntargetedEventParams = function (adid, bnid, cuid) {
		return '&adId=' + adid + '&bnId=' + bnid + '&pId=' + cuid + '&location=' + (encodeURIComponent || escape)(window.loc_.href) + '&cb=' + new Date().getTime();
    };

    adtlgcen.util.extractAdMeta = function (p) {
        var result = null;
        if (p) {
            result = p['adtlgcAdMeta'];
            if (!result) {
                for (var i = 0; i < 2; i++) {
                    var pid = p.getAttribute('en_id');
                    if(!pid){
                    	pid = p.getAttribute('id');
                    }
                    if (pid && (/\d+\$\d+\$\d+/.test(pid))) {
                        var vs = pid.split('$');
                        if (vs.length == 3) {
                            result = {
                                cuid: vs[0],
                                adid: vs[1],
                                bnid: vs[2]
                            };
                            p['adtlgcAdMeta'] = result;
	                    }
                        break;
                    } else {
                        p = p.parentNode;
                    }
                }
            }
        }
        return result;
    };

    adtlgcen.util.populateCampaignData = function (campaignData, campaignDataRef) {
        var result = '';
        var cmMap = [];
        if (campaignData) {
            for (var i = 0; i < campaignData.length; i++) {
                if (campaignData[i]) {
                    var cmpRec = campaignData[i].split(':');
                    result += cmpRec[0] + '|';
                    cmMap[cmpRec[1]] = cmpRec[0];
                }
            }
            window[enr_vars.cmpmap] = cmMap;
        }
        if (campaignDataRef) {
        	cmMap = [];
            for (var i = 0; i < campaignDataRef.length; i++) {
                if (campaignDataRef[i]) {
                    var cmpRec = campaignDataRef[i].split(':');
                    result += cmpRec[0] + '|';
                    cmMap[cmpRec[1]] = cmpRec[0];
                }
            }
            window[enr_vars.cmpmap_ref] = cmMap;
        }
        return result;
    };

    adtlgcen.util.populateCampaignData2 = function (campaignData, campaignDataRef) {
        var result = '';
        var cmstr = '';
        var cmMap = [];
        if (campaignData) {
            for (var key in campaignData) {
                if (campaignData[key]) {
                	var cmIds = campaignData[key].split('|');
                    result += cmIds[0] + '|';
                    cmMap[cmIds[1]] = cmIds[0];
                    if (cmstr) cmstr += ',';
                    cmstr += cmIds[0] + ':' + cmIds[1];
                }
            }
            window['cmstr'] = cmstr; //extId:extId:extId.....
            window[enr_vars.cmpmap] = cmMap; // [extId:ecID, extId:ecId ....]
        }
        if (campaignDataRef) {
        	cmstr = '';
            cmMap = [];
            for (var key in campaignDataRef) {
                if (campaignDataRef[key]) {
                	var cmIds = campaignDataRef[key].split('|');
                	result += cmIds[1] + '|';
                    cmMap[cmIds[1]] = cmIds[0];
                    if (cmstr) cmstr += ',';
                    cmstr += cmIds[0] + ':' + cmIds[1];
                }
            }
            window['cmstr_ref'] = cmstr; //extId:extId:extId.....
            window[enr_vars.cmpmap_ref] = cmMap; // [extId:ecID, extId:ecId ....]
        }
        return result; //ecId|ecId|ecId....
    };

     adtlgcen.util.populateSegmentsTargetingArr = function (segmentsData) {
    	var segmentsArr = [];
    	if(segmentsData){
    		var segmentPairs = segmentsData.split('-');
    		var result = [];
    		for(var i=0;i<segmentPairs.length;i++){
    			var segmentTokens = segmentPairs[i].split(':');
    			var key = segmentTokens[0];
    			var value = segmentTokens[1];
    			if(!result[key]){
    				result[key] = [];
    			}
    			result[key][result[key].length] = value;
    		}
    		for(var key in result){
    			segmentsArr[segmentsArr.length] = {'key':key, 'value':result[key]};
    		}
    	}
    	return segmentsArr;
    };

    adtlgcen.util.en_smart_decode = function(url){
    	var tokens = url.split('%');
    	var result = tokens[0];
    	for(var i=1; i< tokens.length; i++){
    		try{
    			if(tokens[i].length==2 && tokens.length > i && tokens[i+1].length>=2){
    				try{
    					result+=decodeURIComponent('%'+tokens[i]+'%'+tokens[i+1].substring(0,2));
    					result+=tokens[i+1].substring(2, tokens[i+1].length);
    					i++;
    					continue;
    				}catch(e){
    					//ignore
    				}
    			}
    			if(tokens[i].length>=2){
    				result+=decodeURIComponent('%'+tokens[i].substring(0,2))+tokens[i].substring(2, tokens[i].length);
    				continue;
    			}
    		}catch(e){
    			//ignore
    		}
    		result+='%'+tokens[i];
    	}
    	return result;
    };

	adtlgcen.util.makePageImpCall = function (pageURL) {
        var statURL = adtlgcen_SETTINGS.TC_URL + '/event/v3/pagestat?location=' +
	    	(encodeURIComponent || escape)(pageURL || window.loc_.href) +
	    	'&cb=' + new Date().getTime();
        adtlgcen.util.send(statURL);

        if(window[enr_vars.evid]=='optout'){
        	return;
        }

        if(adtlgcen_SETTINGS.AR_ENABLED){
        	var result = adtlgcen.util.en_smart_decode(pageURL || window.loc_.href);
        	statURL = adtlgcen_SETTINGS.TC_URL + '/event/v3/arstat?location='+(encodeURIComponent||escape)(result)+'&cb='+new Date().getTime();
        	adtlgcen.util.send(statURL);
        }

        if(enr_cx_SETTINGS.syncEnabled && enr_cx_SETTINGS.cxPrefixes.length > 0){
			var throttleCookie = 'enr_cxense_throttle';
			var minTimeBetweenUpdatesDays = 7;

			var segments = adtlgcen_Cookie.get(enr_vars.adptseg).replace(/-/g, '|').split('|');
			if(segments && segments.length > 0 && segments[0]){
				if (!cX.getCookie(throttleCookie)) {
					var params = [];
					for(var i=0;i < enr_cx_SETTINGS.cxPrefixes.length; i++){
						var prefix = enr_cx_SETTINGS.cxPrefixes[i];

						var profile = [];
						for(var j=0; j < segments.length;j++){
							var curSegTokens = segments[j].split('#');
							if(segmentGroups[curSegTokens[0]]){
								var segmentValue = segmentValues[segments[j].replace('#','=')];
								if(segmentValue){
									profile[profile.length] = {'item': segmentValue, 'group': prefix + segmentGroups[curSegTokens[0]]};
								}
							}
						}
						if(profile.length>0){
							params[params.length] = {'id': window[enr_vars.evid].replace('cx:','').replace(/:/g,'-'), 'cxid': cX.getUserId(), 'profile':profile, 'type': prefix };
						}
					}

					if(params.length > 0){
						var apiUrl = 'https://api.cxense.com/profile/user/external/update?callback={{callback}}'
							+ '&persisted=' + encodeURIComponent(enr_cx_SETTINGS.persistedUpdateQueryId)
							+ '&json=' + encodeURIComponent(cX.JSON.stringify(params));
						cX.jsonpRequest(apiUrl, function(data) {
							//alert(cX.JSON.stringify(data))
						});
						cX.setCookie(throttleCookie, 'throttle', minTimeBetweenUpdatesDays);
					}
				}
			}
		}

        if (window[enr_vars.evid] && '1' != adtlgcen_Cookie.get(enr_vars.cintsent)){
        	adtlgcen.util.send(adtlgcen_SETTINGS.CINT_URL + window[enr_vars.evid].replace(/:/g,'$'), true, true);
        	adtlgcen_Cookie.set(enr_vars.cintsent, '1', 24); //24 hours
        }
    };

    adtlgcen.util.reportPlacementsImp = function () {
    	if ('optout' == window[enr_vars.evid]) {
    		return;
    	}
    	var placement_ids = [];

    	for(var i=0; i<adtlgcen.config.placements.length; i++){
    		var placementId = adtlgcen.config.placements[i];
    		if (!adtlgcen.config.reported_placements[placementId]) {
				adtlgcen.config.reported_placements[placementId] = 1;
				placement_ids[placement_ids.length] = placementId;
			}
    	}
    	if(placement_ids.length > 0){
    		var statURL = adtlgcen_SETTINGS.TC_URL + '/event/v3/placementstat?source=path&values=' +
    			(encodeURIComponent || escape)(placement_ids.join(',')) +
    			'&location=' + (encodeURIComponent || escape)(window.loc_.href) +
    			'&adserverId='+adtlgcen_SETTINGS.ADSERVER_ID +
    			'&cb=' + new Date().getTime();
    		adtlgcen.util.send(statURL);
    		adtlgcen.config.placements=[];
    	}
    };

    if(adtlgcen_SETTINGS.PL_ENABLED){
	    var googletag = googletag || {};
		googletag.cmd = googletag.cmd || [];
	    googletag.cmd.push(function() {
		    googletag.pubads().addEventListener('slotRenderEnded', function(event) {
		    	if(!event || !event.slot){
		    		return;
		    	}
		    	window.en_slots[event.slot.getAdUnitPath()]=1;
			});
		});
    }

    adtlgcen.util.trackPlacements = function(){
    	for(var key in window.en_slots){
    		var placementId = key;
    		if(adtlgcen_SETTINGS.DFP_NETWORK_ID && adtlgcen_SETTINGS.DFP_NETWORK_ALIAS){
    			placementId = placementId.replace(adtlgcen_SETTINGS.DFP_NETWORK_ID, adtlgcen_SETTINGS.DFP_NETWORK_ALIAS);
    		}

    		if (adtlgcen.config.placements.indexOf(placementId) < 0) {
    			adtlgcen.config.placements[adtlgcen.config.placements.length] = placementId;
    		}
    	}

    	adtlgcen.util.reportPlacementsImp();

    	if(window.en_ads.length>0){
        	for(var i=0; i <  window.en_ads.length; i++){
        		window.en_regAD(window.en_ads[i]);
        	}
        	window.en_ads = [];
        }
    };

    adtlgcen.util.processIframes = function(element){
		var iframes = element.getElementsByTagName('iframe');
		if(element.tagName=='IFRAME'){
			try{
				iframes = element.contentDocument.getElementsByTagName('iframe');
			}catch(e){
				//ignore if 3rd party iframe
			}
		}
		for(var f=0; f < iframes.length; f++){
			try{
				var fdocument = iframes[f].contentDocument;
				if(fdocument && !iframes[f].getAttribute('en_processed')){
					fdocument.enr_frame_win = iframes[f].contentWindow || iframes[f].contentDocument.parentWindow;
					adtlgcen.util.assignIframeClickHandlers(iframes[f], fdocument, 4);
				}
			}catch(e){
				//ignore if 3rd party iframe
			}
		}
	}

    adtlgcen.util.processCanvasAds = function(element){
    	var canvasElements = element.getElementsByTagName('canvas');
		for(var f=0; f < canvasElements.length; f++){
			var canvasEl = canvasElements[f];
			if(!canvasEl.getAttribute('en_processed')){
				adtlgcen.util.assignClickHandler(canvasEl, false);
				canvasEl.setAttribute('en_processed','1');
			}
		}
    };

    adtlgcen.util.assignIframeClickHandlers = function(iframeEl, frameDocument, count){
		if(count>0){
			var array = [];
			var elements = frameDocument.getElementsByTagName('div');
			for(var i=0; i<elements.length;i++){ array.push(elements[i]); }
			elements = frameDocument.getElementsByTagName('p');
			for(var i=0; i<elements.length;i++){ array.push(elements[i]); }
			elements = frameDocument.getElementsByTagName('canvas');
			for(var i=0; i<elements.length;i++){ array.push(elements[i]); }
			elements = frameDocument.getElementsByTagName('a');
			for(var i=0; i<elements.length;i++){ array.push(elements[i]); }
			elements = frameDocument.getElementsByTagName('img');
			for(var i=0; i<elements.length;i++){ array.push(elements[i]); }
			elements = frameDocument.getElementsByTagName('svg');
			for(var i=0; i<elements.length;i++){ array.push(elements[i]); }

			var iframes = frameDocument.getElementsByTagName('iframe');
			if(iframes.length > 0){
				adtlgcen.util.processIframes(frameDocument);
			}

			if(array.length > 0){
				for(var d=0; d < array.length; d++){
					var targetElem = array[d];
					adtlgcen.util.assignClickHandler(targetElem, true);
				}
				iframeEl.setAttribute('en_processed','1');
			}else{
				setTimeout(function(){adtlgcen.util.assignIframeClickHandlers(iframeEl, frameDocument, count--);}, 1000);
			}
		}
	};

    adtlgcen.event.reportImp = function (adid, bnid, cuid) {
    	adtlgcen.event.reportEventBase('imp', adid, bnid, cuid);
		adtlgcen.config.bannersToTrack[adid + ':' + bnid] = 'true';
    };

	adtlgcen.event.reportClick = function (adid, bnid, cuid) {
		adtlgcen.event.reportEventBase('click', adid, bnid, cuid);
	};

    adtlgcen.event.reportEventBase = function (event, adid, bnid, cuid, extra_params) {
        var campaignData = adtlgcen.util.composeStdParams(adid, bnid, cuid, adtlgcen_Cookie.get(enr_vars.evid_ref));
        if (campaignData) {
            var event_url = adtlgcen_SETTINGS.TC_URL + '/event/v3/adstat?action=' + event + campaignData + '&adserverId='+adtlgcen_SETTINGS.ADSERVER_ID;
            adtlgcen.util.send(event_url + (extra_params || ''));
        } else {
			campaignData = adtlgcen.util.composeUntargetedEventParams(adid, bnid, cuid);

            var event_url = adtlgcen_SETTINGS.TC_URL + '/event/v3/adstat?action=' + event + campaignData + '&adserverId='+adtlgcen_SETTINGS.ADSERVER_ID;
			adtlgcen.util.send(event_url + (extra_params || ''));
        }
    };

	adtlgcen.event.reportDwellTime = function (adid, bnid, cuid, currInterval, v_imp) {
    	var extra_params = '&timeOnScreen=' + currInterval + (v_imp ? '' : '&v_imp=true');
    	adtlgcen.event.reportEventBase('dwellTime', adid, bnid, cuid, extra_params);
    };

	adtlgcen.event.processEvents = function () {
		var events = adtlgcen.config.eventsCache;
		adtlgcen.config.suspendAdEvents=false;
        for (var i = 0; i < events.length; i++) {
            var data_arr = events[i].split(':');
            if (data_arr[0] == 'imp') {
				adtlgcen.event.reportImp(data_arr[1], data_arr[2], data_arr[3]);
            } else if (data_arr[0] == 'click') {
				adtlgcen.event.reportClick(data_arr[1], data_arr[2], data_arr[3]);
            } else if (data_arr[0] == 'dwell') {
				adtlgcen.event.reportDwellTime(data_arr[1], data_arr[2], data_arr[3], data_arr[4], data_arr[5]);
            }
        }
        adtlgcen.config.eventsCache = [];
        adtlgcen.util.reportPlacementsImp();
    };

	window.alen_onactiveelementchange = function (p) {
        var adMeta = '';
        if (p.enrFrameCont) {
            adMeta = adtlgcen.util.extractAdMeta(p);
        } else {
	        if (!p || !p.tagName || !(p.tagName in adtlgcen.config.tagsToMonitor)) {
	        	return;
	        }

	        for (var i = 0; i < 5; i++) {
	            if (!p) break;
	            if (p.tagName == 'BODY') break;
	            if (p.tagName == 'DIV' && p.id && adtlgcen.util.extractAdMeta(p)) break;
	            p = p.parentNode;
	        }

	        adMeta = adtlgcen.util.extractAdMeta(p);
        }
	        if (adMeta) {
	            var cuid = adMeta['cuid'],
	                adid = adMeta['adid'],
	                bnid = adMeta['bnid'];
	            if (cuid && adid && bnid) {
	                if (adtlgcen.config.bannersToTrack[adid + ':' + bnid]) {
	                    if (adtlgcen.config.suspendAdEvents) {
	                        adtlgcen.config.eventsCache.push('click:' + adid + ':' + bnid + ':' + cuid);
	                        return;
	                    }
	                    adtlgcen.event.reportClick(adid, bnid, cuid);
	                }
	            }
	        }

    };

    adtlgcen.util.assignClickHandler = function(targetElem, useIframeElem){
    	if(!targetElem){
    		targetElem = document;
    	}
    	if (targetElem.attachEvent) {
		    targetElem.attachEvent('onclick', function (e) {
		    	 var activeElement = e.srcElement ? e.srcElement : e.target;
                if (activeElement) {
                    if (window.alen_onactiveelementchange) {
                        if(useIframeElem && activeElement.ownerDocument.enr_frame_win && activeElement.ownerDocument.enr_frame_win.frameElement){
                            activeElement = activeElement.ownerDocument.enr_frame_win.frameElement;
                        }
                        window.alen_onactiveelementchange(activeElement);
                    }
                }
	        }, false);
    	} else {
		    targetElem.addEventListener('click', function (e) {
                var activeElement = e.target;
                if (activeElement) {
                    if (window.alen_onactiveelementchange) {
                        if(useIframeElem && activeElement.ownerDocument.enr_frame_win && activeElement.ownerDocument.enr_frame_win.frameElement){
                         	activeElement = activeElement.ownerDocument.enr_frame_win.frameElement;
                        }
                        window.alen_onactiveelementchange(activeElement);
                    }
                }
	        }, false);
    	}
    };

    adtlgcen.util.assignClickHandler();

    adtlgcen.util.setOriginForIframeADs = function(){
		var iframes = document.getElementsByTagName('IFRAME');
		var requestObj = {'action':'setOrigin','origin':window.loc_.origin};
		for(var i=0;i<iframes.length;i++){
			try{
				if(iframes[i].src && iframes[i].src.indexOf('https://tpc.googlesyndication.com')>-1){
					iframes[i].contentWindow.postMessage(JSON.stringify(requestObj), 'https://tpc.googlesyndication.com');
				}
			}catch(e){
				//ignore
			}
		}
		setTimeout(adtlgcen.util.setOriginForIframeADs, 1000);
	};

    adtlgcen.util.optOut = function(skipAutidLog){
		adtlgcen_Cookie.unset(enr_vars.evid_v);
		adtlgcen_Cookie.unset(enr_vars.adptset);
		adtlgcen_Cookie.unset(enr_vars.adptseg);
		adtlgcen_Cookie.unset(enr_vars.adptcmp);
		adtlgcen_Cookie.unset(enr_vars.adptcmp_ref);
		window[enr_vars.evid] = 'optout';

		if(!skipAutidLog){
			var auditURL = adtlgcen_SETTINGS.TC_URL + '/audit-log?action=optout&date=' + new Date().getTime() + '&site=' + document.domain;
			adtlgcen.util.send(auditURL);
		}
    };

    adtlgcen.util.optIn = function(){
		var url = adtlgcen_SETTINGS.TC_URL + '/user?nw=0&callback=' + enr_vars.dataRequest + '.enCallback&cb=' + new Date().getTime();
        window[enr_vars.dataRequest].send(url, false);

        var auditURL = adtlgcen_SETTINGS.TC_URL + '/audit-log?action=optin&date=' + new Date().getTime() + '&site=' + document.domain;
        adtlgcen.util.send(auditURL);
    };

	window.admp_ = new function () {
        this.inViewNode = new InViewNode({});

		this.inViewCallback = function (element) {
			if (element) {
			    var adMeta = adtlgcen.util.extractAdMeta(element);
			    var cuid = adMeta['cuid'],
			        adid = adMeta['adid'],
			        bnid = adMeta['bnid'];
			    if (bnid && element[enr_vars.inview_data_container].sentDwellTime < adtlgcen_SETTINGS.maxDwellTimeToSend) {
			        var percOfVisibility = element[enr_vars.inview_data_container].width * element[enr_vars.inview_data_container].height / 100;
			        if (percOfVisibility >= adtlgcen_SETTINGS.minPercOfVisibility) {
			            var visibleTime = parseInt((element[enr_vars.inview_data_container].time) / 1000, 10);
			            var currInterval = adtlgcen.util.interval(element[enr_vars.inview_data_container].iteration);
			            var toSend = adtlgcen_SETTINGS.maxDwellTimeToSend - element[enr_vars.inview_data_container].sentDwellTime;
			            if (currInterval > toSend) {
			                currInterval = toSend;
			            }
			            if (visibleTime > 0 && (visibleTime % currInterval == 0)) {
							var v_imp = element[enr_vars.inview_data_container].sentDwellTime;
				            if (adtlgcen.config.suspendAdEvents) {
			                    adtlgcen.config.eventsCache.push('dwell:' + adid + ':' + bnid + ':' + cuid + ':' + currInterval + ':' + v_imp);
			                } else {
			                    adtlgcen.event.reportDwellTime(adid, bnid, cuid, currInterval, v_imp);
			                }
			                element[enr_vars.inview_data_container].start_ = 0;
			                element[enr_vars.inview_data_container].time = 0;
			                element[enr_vars.inview_data_container].iteration++;
			                element[enr_vars.inview_data_container].sentDwellTime += currInterval;
			            }
			        } else {
			            element[enr_vars.inview_data_container].start_ = 0;
			            element[enr_vars.inview_data_container].time = 0;
			            element[enr_vars.inview_data_container].iteration = 1;
			        }
			    }
			}
		};

		this.initTracking = function (contId) {
            var adContainer;
            if (typeof contId === 'string') {
                adContainer = document.getElementById(contId);
            } else {
                adContainer = contId;
            }
            var adMeta = adtlgcen.util.extractAdMeta(adContainer);
            if (adMeta) {
                var cuid = adMeta['cuid'],
                    adid = adMeta['adid'],
                    bnid = adMeta['bnid'];
                if (bnid) {
                    if (adtlgcen.config.suspendAdEvents) {
						adtlgcen.config.eventsCache.push('imp:' + adid + ':' + bnid + ':' + cuid);
                    } else {
						adtlgcen.event.reportImp(adid, bnid, cuid);
                    }
                    adtlgcen.config.bannersToTrack[adid + ':' + bnid] = 'true';
                    adtlgcen.config.blocksToTrack.push(adContainer);
                    this.inViewNode.addElementToTrack(adContainer);
                }
            }
		};
    };

	admp_.init = function() {
		adtlgcen.util.setOriginForIframeADs();

		if(!cX.isConsentRequired() || (cX.isConsentRequired() && cX.hasConsent('pv'))){
			window[enr_vars.evid] = cX.getCxenseUserId();
			adtlgcen_Cookie.set(enr_vars.evid, window[enr_vars.evid], 90 * 24);
			window[enr_vars.evid_v] = adtlgcen_Cookie.get(enr_vars.evid_v);

			var campaignsData = adtlgcen_Cookie.get(enr_vars.adptcmp);
			window[enr_vars.adptseg] = adtlgcen_Cookie.get(enr_vars.adptseg).replace(/#/g, '=');
			window[enr_vars.segm_targeting_var] = adtlgcen_Cookie.get(enr_vars.adptseg).replace(/#/g, ':');

			var skipPageImp = false;

			if (campaignsData || window[enr_vars.adptseg]) {
				var result = adtlgcen.util.populateCampaignData(campaignsData.split(','));
				if ('optout' == window[enr_vars.evid]) {
					window[enr_vars.adptseg] = window[enr_vars.targeting_var] = window[enr_vars.segm_targeting_var] = '';
					window[enr_vars.segmArr_targeting_var] = [];
				} else {
					window[enr_vars.targeting_var] = result;
					window[enr_vars.segmArr_targeting_var] = adtlgcen.util.populateSegmentsTargetingArr(window[enr_vars.segm_targeting_var]);
				}
			} else if ('1' != adtlgcen_Cookie.get(enr_vars.adptset)) {
				skipPageImp = true;
				var url = adtlgcen_SETTINGS.TC_URL + '/user?sg=1&nw=0&callback=' + enr_vars.dataRequest + '.enCallback&cb=' + new Date().getTime();
				window[enr_vars.dataRequest].send(url);
			}

			if(!skipPageImp){
	        	adtlgcen.util.makePageImpCall();
	        	adtlgcen.event.processEvents();
			}
		}else{
			adtlgcen.util.optOut(true);
			adtlgcen.util.makePageImpCall();
		}

		var eventMethod = window.addEventListener ? 'addEventListener' : 'attachEvent';
    	var eventer = window[eventMethod];
    	var messageEvent = eventMethod == 'attachEvent' ? 'onmessage' : 'message';
    	eventer(messageEvent, function (e) {
    		try{
    			var responseObj = JSON.parse(e.data);
    			if(responseObj['action']=='ad_event'){
    				var url = responseObj['url'];
    				url += '&location=' + (encodeURIComponent || escape)(window.loc_.href);
    				adtlgcen.util.send(url);
    			}
    		}catch(e){
    			//ignore
    		}
    	}, false);

        this.inViewNode.init(adtlgcen.config.blocksToTrack, this.inViewCallback);
        admp_.plDetector = new PlacementDetector;
        if(adtlgcen_SETTINGS.PL_ENABLED){
        	admp_.plDetector.init();
        }
        admp_.dynamicLoadingDetector = new DynamicLoadingDetector;
        admp_.dynamicLoadingDetector.init();
    };

	function DataRequest(functionInstance) {
        this.fn = functionInstance;

        this.send = function (url, sendEvid) {
        	if (sendEvid || window[enr_vars.evid]) {
        		url += '&evid=' + window[enr_vars.evid];
        	}
            url += adtlgcen_SETTINGS.SCRIPT_VERSION_PARAMETER;

            var scriptNode = document.createElement('script');
            scriptNode.setAttribute('type', 'text/javascript');
            scriptNode.setAttribute('charset', 'utf-8');
            scriptNode.setAttribute('src', url);
            document.getElementsByTagName('head')[0].appendChild(scriptNode);
        };

        this.enCallback = function (data) {
			if (data && data['id']) {
                if(data['vv']){
                	adtlgcen_Cookie.set(enr_vars.evid_v, data['vv'], 90 * 24);
                	window[enr_vars.evid_v] = adtlgcen_Cookie.get(enr_vars.evid_v);
                }
            }
        	if (window[enr_vars.evid]) {
				if ((data['camp'] || data['campref']) && 'optout' != data['id']) {
                	var campaignData = data['camp'];
                	var campaignDataRef = data['campref'];

                	adtlgcen.util.populateCampaignData2(campaignData, campaignDataRef);
                    if (window['cmstr']) {
                        adtlgcen_Cookie.set(enr_vars.adptcmp, window['cmstr'], 2);
                    }
                    if (window['cmstr_ref']) {
                        adtlgcen_Cookie.set(enr_vars.adptcmp_ref, window['cmstr_ref'], 2);
                    }
                }
                var segmentsData = data['segm'];
                if (segmentsData) {
                    var segments='';
                    for (var i = 0; i < segmentsData.length; i++) {
                        if (segmentsData[i]) {
                            if(segments){
                                segments+=';';
                            }
                            segments += segmentsData[i];
                        }
                    }
                    if (segments) {
                        window[enr_vars.segm_targeting_var] = segments.replace(/;/g, '-').replace(/=/g, ':');
                        window[enr_vars.segmArr_targeting_var] = adtlgcen.util.populateSegmentsTargetingArr(window[enr_vars.segm_targeting_var]);
                        adtlgcen_Cookie.set(enr_vars.adptseg, segments.replace(/;/g, '-').replace(/=/g, '#'), 2);
                        window[enr_vars.adptseg] = segments.replace(/;/g, '-');
                    }
                }

                adtlgcen.event.processEvents();
	        }
        	adtlgcen_Cookie.set(enr_vars.adptset, '1', 2);
			adtlgcen.util.makePageImpCall();
        };
    };

    window[enr_vars.dataRequest] = new DataRequest(admp_);

    cX.callQueue.push(['invoke', function() {
    	(function exec(){
    		if(cX.getCxenseUserId && cX.getCxenseUserId()){
    			admp_.init();
    		}else{
    			setTimeout(function(){exec()}, 50);
    		}
    	}());
    }]);
}