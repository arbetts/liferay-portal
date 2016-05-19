define("frontend-js-metal-web@1.0.6/metal-component/src/Component-min", ["exports","metal/src/metal","metal-dom/src/all/dom","./ComponentRegistry","./ComponentRenderer","metal-events/src/events","metal-state/src/State"], function(e,t,n,o,r,s,i){"use strict";function a(e){return e&&e.__esModule?e:{"default":e}}function l(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function c(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}function d(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}Object.defineProperty(e,"__esModule",{value:!0});var p=a(o),h=a(r),u=a(i),m=function(e){function o(n,r){l(this,o);var i=c(this,e.call(this,n));return (i.attachedListeners_={}, i.components={}, i.elementEventProxy_=null, i.eventsStateKeyHandler_=new s.EventHandler, i.inDocument=!1, i.initialConfig_=n||{}, i.wasRendered=!1, i.DEFAULT_ELEMENT_PARENT=document.body, t.core.mergeSuperClassesProperty(i.constructor,"ELEMENT_CLASSES",i.mergeElementClasses_), i.renderer_=i.createRenderer(), i.renderer_.on("rendered",i.rendered.bind(i)), i.on("stateChanged",i.handleStateChanged_), i.newListenerHandle_=i.on("newListener",i.handleNewListener_), i.on("eventsChanged",i.onEventsChanged_), i.addListenersFromObj_(i.events), i.created(), r!==!1&&i.render_(r), i.on("elementChanged",i.onElementChanged_), i)}return (d(o,e), o.prototype.addElementClasses_=function(){var e=this.constructor.ELEMENT_CLASSES_MERGED;this.elementClasses&&(e=e+" "+this.elementClasses),n.dom.addClasses(this.element,e)}, o.prototype.addListenersFromObj_=function(e){for(var t=Object.keys(e||{}),n=0;n<t.length;n++){var o=this.extractListenerInfo_(e[t[n]]);if(o.fn){var r;r=o.selector?this.delegate(t[n],o.selector,o.fn):this.on(t[n],o.fn),this.eventsStateKeyHandler_.add(r)}}}, o.prototype.attach=function(e,t){return (this.inDocument||(this.renderElement_(e,t),this.inDocument=!0,this.emit("attached",{parent:e,sibling:t}),this.attached()), this)}, o.prototype.attached=function(){}, o.prototype.addSubComponent=function(e,n,o){var r=n;t.core.isString(r)&&(r=p["default"].getConstructor(n));var s=this.components[e];return (s&&s.constructor!==r&&(s.dispose(),s=null), s||(this.components[e]=new r(o,!1)), this.components[e])}, o.prototype.created=function(){}, o.prototype.createRenderer=function(){return (t.core.mergeSuperClassesProperty(this.constructor,"RENDERER",t.array.firstDefinedValue), new this.constructor.RENDERER_MERGED(this))}, o.prototype.delegate=function(e,t,n){return this.on("delegate:"+e+":"+t,n)}, o.prototype.detach=function(){return (this.inDocument&&(this.element&&this.element.parentNode&&this.element.parentNode.removeChild(this.element),this.inDocument=!1,this.detached()), this.emit("detached"), this)}, o.prototype.detached=function(){}, o.prototype.disposed=function(){}, o.prototype.disposeInternal=function(){this.disposed(),this.detach(),this.elementEventProxy_&&(this.elementEventProxy_.dispose(),this.elementEventProxy_=null),this.disposeSubComponents(Object.keys(this.components)),this.components=null,this.renderer_.dispose(),this.renderer_=null,e.prototype.disposeInternal.call(this)}, o.prototype.disposeSubComponents=function(e){for(var t=0;t<e.length;t++){var n=this.components[e[t]];n.isDisposed()||(n.dispose(),delete this.components[e[t]])}}, o.prototype.extractListenerInfo_=function(e){var n={fn:e};return (t.core.isObject(e)&&!t.core.isFunction(e)&&(n.selector=e.selector,n.fn=e.fn), t.core.isString(n.fn)&&(n.fn=this.getListenerFn(n.fn)), n)}, o.prototype.getInitialConfig=function(){return this.initialConfig_}, o.prototype.getListenerFn=function(e){return t.core.isFunction(this[e])?this[e].bind(this):void 0}, o.prototype.fireStateKeyChange_=function(e,n){var o=this["sync"+e.charAt(0).toUpperCase()+e.slice(1)];t.core.isFunction(o)&&(n||(n={newVal:this[e],prevVal:void 0}),o.call(this,n.newVal,n.prevVal))}, o.prototype.getRenderer=function(){return this.renderer_}, o.prototype.handleStateChanged_=function(e){this.syncStateFromChanges_(e.changes),this.emit("stateSynced",e)}, o.prototype.handleNewListener_=function(e){this.attachedListeners_[e]=!0}, o.prototype.mergeElementClasses_=function(e){var t={};return e.filter(function(e){return!e||t[e]?!1:(t[e]=!0,!0)}).join(" ")}, o.prototype.onElementChanged_=function(e){e.prevVal!==e.newVal&&(this.setUpProxy_(),this.elementEventProxy_.setOriginEmitter(e.newVal),this.addElementClasses_(),this.syncVisible(this.visible))}, o.prototype.onEventsChanged_=function(e){this.eventsStateKeyHandler_.removeAllListeners(),this.addListenersFromObj_(e.newVal)}, o.prototype.render_=function(e,t){t||this.emit("render"),this.setUpProxy_(),this.syncState_(),this.attach(e),this.wasRendered=!0}, o.prototype.renderAsSubComponent=function(){this.render_(null,!0)}, o.prototype.renderElement_=function(e,t){var o=this.element;if(o&&(t||!o.parentNode)){var r=n.dom.toElement(e)||this.DEFAULT_ELEMENT_PARENT;r.insertBefore(o,n.dom.toElement(t))}}, o.prototype.setterElementFn_=function(e,t){var o=e;return (o&&(o=n.dom.toElement(e)||t), o)}, o.prototype.setUpProxy_=function(){if(!this.elementEventProxy_){var e=new n.DomEventEmitterProxy(this.element,this);this.elementEventProxy_=e,t.object.map(this.attachedListeners_,e.proxyEvent.bind(e)),this.attachedListeners_=null,this.newListenerHandle_.removeListener(),this.newListenerHandle_=null}}, o.prototype.syncState_=function(){for(var e=this.getStateKeys(),t=0;t<e.length;t++)this.fireStateKeyChange_(e[t])}, o.prototype.syncStateFromChanges_=function(e){for(var t in e)this.fireStateKeyChange_(t,e[t])}, o.prototype.syncElementClasses=function(e,t){this.element&&t&&n.dom.removeClasses(this.element,t),this.addElementClasses_()}, o.prototype.syncVisible=function(e){this.element&&(this.element.style.display=e?"":"none")}, o.prototype.rendered=function(){}, o.prototype.validatorElementClassesFn_=function(e){return t.core.isString(e)}, o.prototype.validatorElementFn_=function(e){return t.core.isElement(e)||t.core.isString(e)||!t.core.isDefAndNotNull(e)}, o.prototype.validatorEventsFn_=function(e){return!t.core.isDefAndNotNull(e)||t.core.isObject(e)}, o)}(u["default"]);m.STATE={element:{setter:"setterElementFn_",validator:"validatorElementFn_"},elementClasses:{validator:"validatorElementClassesFn_"},events:{validator:"validatorEventsFn_",value:null},visible:{validator:t.core.isBoolean,value:!0}},m.ELEMENT_CLASSES="",m.RENDERER=h["default"],m.INVALID_KEYS=["components","wasRendered"],e["default"]=m});