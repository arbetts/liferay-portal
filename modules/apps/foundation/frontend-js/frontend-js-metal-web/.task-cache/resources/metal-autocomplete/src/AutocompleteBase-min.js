define("frontend-js-metal-web@1.0.8/metal-autocomplete/src/AutocompleteBase-min", ["exports","metal/src/metal","metal-dom/src/all/dom","metal-promise/src/promise/Promise","metal-component/src/all/component","metal-events/src/events","metal-jquery-adapter/src/JQueryAdapter"], function(e,t,n,r,o,i,a){"use strict";function u(e){return e&&e.__esModule?e:{"default":e}}function s(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function l(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}function c(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}Object.defineProperty(e,"__esModule",{value:!0});var f=u(t),p=u(n),d=u(r),h=u(o),m=u(a),y=function(e){function t(){return (s(this,t), l(this,e.apply(this,arguments)))}return (c(t,e), t.prototype.created=function(){this.eventHandler_=new i.EventHandler,this.on("select",this.select)}, t.prototype.attached=function(){this.inputElement&&this.eventHandler_.add(p["default"].on(this.inputElement,"input",this.handleUserInput_.bind(this)))}, t.prototype.detached=function(){this.eventHandler_.removeAllListeners()}, t.prototype.handleUserInput_=function(){this.request(this.inputElement.value)}, t.prototype.request=function(e){var t=this;this.pendingRequest&&this.pendingRequest.cancel("Cancelled by another request");var n=t.data(e);return (f["default"].isPromise(n)||(n=d["default"].resolve(n)), this.pendingRequest=n.then(function(e){return Array.isArray(e)?e.map(t.format.bind(t)).filter(function(e){return f["default"].isDefAndNotNull(e)}):void 0}), this.pendingRequest)}, t.prototype.setData_=function(e){return f["default"].isFunction(e)?e:function(){return e}}, t)}(h["default"]);y.STATE={data:{setter:"setData_"},format:{value:f["default"].identityFunction,validator:f["default"].isFunction},inputElement:{setter:p["default"].toElement},select:{value:function(e){this.inputElement.value=e.text,this.inputElement.focus()},validator:f["default"].isFunction},visible:{validator:f["default"].isBoolean,value:!1}},e["default"]=y,m["default"].register("autocompleteBase",y)});