/*
 * cx-custom.js
 * Copyright (C) 2017 Cxense ASA
 *
 * Usage: 
 *   0. Initialize custom trackings
 *     window.cXcustom = window.cXcustom || function() {(window.cXcustom.q = window.cXcustom.q || []).push(arguments)};
 *     cXcustom("<command>", args1, args2, args3, ...);
 *     cX.callQueue.push(['invoke', function() {
 *       cX.loadScript("<path>/cx-custom.js");
 *     }]);
 *
*   1. Scroll depth tracking 2 (selector ver.)
 *     Start scroll depth tracking:
 *       cXcustom("scrollDepth", eventHandler, trackingMethod, selector, baselineDelay);
 *     eventHandler: callback function to be called when scroll depth reaches 25%, 50%, 75%, 100%
 *       Note that 0% will be passed to the function when the tracker is ready
 *     trackingMethod:
 *       0 - measure percentage relative to the entire page
 *       1 - measure percentage of article
 *       2 - measure percentage relative to the end of the article
 *     selector: Selector for the element (N/A for trackingMethod == 0)
 *       if trackingMethod == 1: element encapuslating the article body
 *       if trackingMethod == 2: element at the end of the article
 *     selector: selector of target element
 *     baselineDelay: delay in milliseconds before eventHandler is called for baseline, -1 to disable
 *       'baseline' is the event(s) sent to report initial visibility on first view without requiring the user to scroll
 *
*/

(function() {

  if(_isRobot()) {
    return;
  }

   /**
   * Setup scroll depth tracking
   */
  function initCxScrollDepth(eventHandler, trackingMethod, selector, baselineDelay) {
    var cxScrollDepth = (function() {
      var _instance;
      var _eventHandler;
      var _element;
      var _resolution;
      var _prevVal;
      var _calcDepth;
      // setup
      function _init(eventHandler, trackingMethod, selector, baselineDelay) {
        _eventHandler = eventHandler;
        if (trackingMethod != 0) {
          _element = document.querySelector(selector);
          if(!_element) {
            return;
          }
        }
        _resolution = 25;
        _prevVal = 0;
        switch (trackingMethod) {
          case 0:
            _calcDepth = _calcDepth0;
            break;
          case 1:
            _calcDepth = _calcDepth1;
            break;
          default:
            _calcDepth = _calcDepth2;
            break;
        }
        _startScrollDepthTracking(baselineDelay);
      };
      return {
        init: function (eventHandler, trackingMethod, selector, baselineDelay) {
          _init(eventHandler, trackingMethod, selector, baselineDelay);
        }
      };
      function _calcDepth0() {
        var pos = cX.getScrollPos();
        var windowSize = cX.getWindowSize();
        var docSize = cX.getDocumentSize();
        var percentage = (pos.top + windowSize.height) / docSize.height * 100;
        percentage = Math.ceil(percentage);
        if (percentage > 100) { percentage = 100; }
        var depth = Math.floor(percentage / _resolution);
        var reportVal = depth * _resolution;
        if (_prevVal < reportVal && reportVal >= _resolution) {
          for (var val = _prevVal + _resolution; val < reportVal; val += _resolution) {
            _eventHandler(val);
          }
          _eventHandler(reportVal);
          _prevVal = reportVal;
        }
      }
      function _calcDepth1() {
        var pos = cX.getScrollPos();
        var windowSize = cX.getWindowSize();
        var bodyElementPos = cX.getElementPosition(_element);
        if (pos.top + windowSize.height > bodyElementPos.top) {
          var percentage = (pos.top + windowSize.height - bodyElementPos.top) / _element.offsetHeight * 100;
          percentage = Math.ceil(percentage);
          if (percentage > 100) { percentage = 100; }
          var depth = Math.floor(percentage / _resolution);
          var reportVal = depth * _resolution;
          if (_prevVal < reportVal && reportVal >= _resolution) {
            for (var val = _prevVal + _resolution; val < reportVal; val += _resolution) {
              _eventHandler(val);
            }
            _eventHandler(reportVal);
            _prevVal = reportVal;
          }
        }
      }
      function _calcDepth2() {
        var pos = cX.getScrollPos();
        var windowSize = cX.getWindowSize();
        var bodyElementPos = cX.getElementPosition(_element);
        var percentage = (pos.top + windowSize.height) / bodyElementPos.top * 100;
        percentage = Math.ceil(percentage);
        if (percentage > 100) { percentage = 100; }
        var depth = Math.floor(percentage / _resolution);
        var reportVal = depth * _resolution;
        if (_prevVal < reportVal && reportVal >= _resolution) {
          for (var val = _prevVal + _resolution; val < reportVal; val += _resolution) {
            _eventHandler(val);
          }
          _eventHandler(reportVal);
          _prevVal = reportVal;
        }
      }
      function _onScroll() {
        if (_prevVal >= 100) return;
        _calcDepth();
      };
      /*
       * Throttle function borrowed from:
       * Underscore.js 1.5.2
       * http://underscorejs.org
       * (c) 2009-2013 Jeremy Ashkenas, DocumentCloud and Investigative Reporters & Editors
       * Underscore may be freely distributed under the MIT license.
       */
      function throttle(func, wait) {
        var context, args, result;
        var timeout = null;
        var previous = 0;
        var later = function() {
          previous = new Date;
          timeout = null;
          result = func.apply(context, args);
        };
        return function() {
          var now = new Date;
          if (!previous) previous = now;
          var remaining = wait - (now - previous);
          context = this;
          args = arguments;
          if (remaining <= 0) {
            clearTimeout(timeout);
            timeout = null;
            previous = now;
            result = func.apply(context, args);
          } else if (!timeout) {
            timeout = setTimeout(later, remaining);
          }
          return result;
        };
      }
      function _startScrollDepthTracking(baselineDelay) {
        _eventHandler(0);
        if (baselineDelay > 0) {
          setTimeout(function() {
            _onScroll();
          }, baselineDelay);
        }
        window.addEventListener('scroll', throttle(_onScroll, 500), false);
      };
    })();
    if (document.readyState === "loading") {
      window.document.addEventListener("DOMContentLoaded", function (event) {
        cxScrollDepth.init(eventHandler, trackingMethod, selector, baselineDelay);
      });
    } else {
      cxScrollDepth.init(eventHandler, trackingMethod, selector, baselineDelay);
    }
  }

  var cxExternalLinkTracker = (function(){
    var _cpMaxLen = 256;
    var _eventHandler;
    var _targetArea = {
      "arti-body": "記事本文"
    };
    function _init(eventHandler) {
      var tieup=false; // =false
      var metas=document.getElementsByTagName("meta");
      for(var i=0; i<metas.length; i++) {
        if(metas[i].name === "cXenseParse:iid-article-type" && metas[i].content === "PR記事"){
          tieup=true;
          break;
        }
      }
      if(tieup) {
        _eventHandler = eventHandler;
        _startTracking();

      }
    }

    function _startTracking() {
      window.addEventListener('mouseup', _onClickEvent, true);
      window.addEventListener('contextmenu', _onClickEvent, true);
    }

    function _stopTracking() {
      window.removeEventListener('mouseup', _onClickEvent, true);
      window.removeEventListener('contextmenu', _onClickEvent, true);
    }

    function _onClickEvent(event) {
      try {
        var ev = event || window.event;
        var href = "";
        var text = "";
        if (ev) {
          var el = ev.target || ev.srcElement;
          for(var i = 0; (i < 10) && (el) && el.nodeName.toLowerCase() !== 'body'; i++) {
            if(el.nodeType === 1 && el.nodeName.toLowerCase() === 'a') {
              if(typeof el.href === 'string') {
                href = el.href;
              }
              if(text.length == 0 && el.innerText.length > 0) {
                text = el.innerText;
              }
            }
            var areaName = _getAreaName(el, el.parentNode ? el.parentNode.childNodes : []);
            if(areaName) {
              break;
            }
            el = el.parentNode;
          }
          if(areaName && href) {
            text = text || href;
            _eventHandler(
              areaName.slice(0, _cpMaxLen),
              {
                "text": text.slice(0, _cpMaxLen),
                "href": href.replace(/^https?:\/\//, '').slice(0, _cpMaxLen)
              }
            );
          }
        }
      } catch (e) { }
      return true;
    }
    function _getAreaName(el, neighborNodes) {
      if(el.className) {
        var classNames = el.className.split(" ");
        for(var i=0; i<classNames.length; i++) {
          if(Object.keys(_targetArea).indexOf(classNames[i])!=-1) {
            return _targetArea[classNames[i]];
          }
        }
      }
    }
    return {
      init: function(eventHandler) {
        _init(eventHandler);
      }
    }
  })();

  function _isRobot() {
    var robot_strings = ["preview", "phantomjs", "spider", "crawl", "cxense", "slurp", "read ahead agent"];
    var re_bot = /\bbot|bot\b/;
    var ua = navigator.userAgent.toLowerCase();
    if(re_bot.test(ua)) {
      return true;
    } else {
      for(var i=0; i<robot_strings.length; i++) {
        if(ua.indexOf(robot_strings[i])!=-1) {
          return true;
        }
      }
    }
    return ua.indexOf("mozilla") == 0 && ua.indexOf(" ") == -1;
  }

  // console.log("cx-custom.js loaded!");
  var _cXcustom = function () { (window.cXcustom.q = window.cXcustom.q || []).push(arguments); _queueExecute(); };
  _cXcustom.q = window.cXcustom && window.cXcustom.q ? window.cXcustom.q : [];
  window.cXcustom = _cXcustom;

  _queueExecute();

  function _queueExecute() {
    while (window.cXcustom.q && window.cXcustom.q.length > 0) {
      var command = window.cXcustom.q.shift();
      try {
        if (command[0] === "scrollDepth2") {
          initCxScrollDepth(command[1], command[2], command[3], command[4])
        }else if(command[0] === "externalLink") {
          cxExternalLinkTracker.init(command[1]);
        }
      } catch (e) { }
    }
  }
})();
