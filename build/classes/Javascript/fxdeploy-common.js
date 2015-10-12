/**
 @preserve
 Copyright (c) 2015, Malcolm Lidierth, UK.
 All rights reserved.

 Modifications for use with AppReflector etc.
 Copyright (c) 2015, AppReflector Ltd, UK.
 All rights reserved.
 */

/**
 Redistribution and use in source and binary forms, with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and
 the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 and the following disclaimer in the documentation and/or other materials provided with the distribution.

 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 or promote products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 **/

//08.09/2015

var fxdeploy$common;
fxdeploy$common = function () {

    /**
     * Location of the Java jnlp file.
     * Set this to the appropriate location for the host web-site.
     * The value can be changed from a web-page using
     *       fxdeploy$common.setJnlpFile(filename)
     */
    var default_jnlp_file = '../../RuntimeResources/FXDeployment/dist/FXDeployment.jnlp';
    // For the AppReflector.com demo pages
    //var jnlp_file = './java/FXDeploy/dist/FXDeploy.jnlp';

    /**
     * Base64 encoded contents of the Java jnlp file.
     * Reset to null when setJnlpFile is called.
     */
    var jnlp_base64 = null;
    //jnlp_base64 = 'PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPGpubHAgc3BlYz0iMS4wIiB4bWxuczpqZng9Imh0dHA6Ly9qYXZhZnguY29tIiBocmVmPSJGWERlcGxveS5qbmxwIj4KICA8aW5mb3JtYXRpb24+CiAgICA8dGl0bGU+RlhEZXBsb3k8L3RpdGxlPgogICAgPHZlbmRvcj5BcHBSZWZsZWN0b3IgTHRkPC92ZW5kb3I+CiAgICA8ZGVzY3JpcHRpb24+SmF2YUZYIGRlcGxveW1lbnQgZm9yIHRoZSB3ZWIgYW5kIGRlc2t0b3A8L2Rlc2NyaXB0aW9uPgogICAgPG9mZmxpbmUtYWxsb3dlZC8+CiAgPC9pbmZvcm1hdGlvbj4KICA8cmVzb3VyY2VzPgogICAgPGoyc2UgdmVyc2lvbj0iMS42KyIgaHJlZj0iaHR0cDovL2phdmEuc3VuLmNvbS9wcm9kdWN0cy9hdXRvZGwvajJzZSIvPgogICAgPGphciBocmVmPSJGWERlcGxveS5qYXIiIHNpemU9IjE1NTE4IiBkb3dubG9hZD0iZWFnZXIiIC8+CiAgICA8amFyIGhyZWY9ImxpYi93YXRlcmxvb0ZYLTAuOC1TTkFQU0hPVC5qYXIiIHNpemU9IjI4ODU0NCIgZG93bmxvYWQ9ImVhZ2VyIiAvPgogIDwvcmVzb3VyY2VzPgo8c2VjdXJpdHk+CiAgPGFsbC1wZXJtaXNzaW9ucy8+Cjwvc2VjdXJpdHk+CiAgPGFwcGxldC1kZXNjICB3aWR0aD0iODAwIiBoZWlnaHQ9IjYwMCIgbWFpbi1jbGFzcz0iY29tLmphdmFmeC5tYWluLk5vSmF2YUZYRmFsbGJhY2siICBuYW1lPSJGWERlcGxveSIgPgogICAgPHBhcmFtIG5hbWU9InJlcXVpcmVkRlhWZXJzaW9uIiB2YWx1ZT0iOC4wKyIvPgogIDwvYXBwbGV0LWRlc2M+CiAgPGpmeDpqYXZhZngtZGVzYyAgd2lkdGg9IjgwMCIgaGVpZ2h0PSI2MDAiIG1haW4tY2xhc3M9ImNvbS5hcHByZWZsZWN0b3IuY29tbW9uLkZYV2ViQXBwIiAgbmFtZT0iRlhEZXBsb3kiIC8+CiAgPHVwZGF0ZSBjaGVjaz0iYWx3YXlzIi8+Cjwvam5scD4K';


    /**
     * Standard header to use for FXML.
     * The getHeader and setHeader methods allows this to be user-edited
     *
     * @type {string}
     */
    var header = '<?xml version="1.0" encoding="UTF-8"?>\n'
        + '\n'
        + '<?import java.lang.*?>\n'
        + '<?import java.util.*?>\n'
        + '<?import javafx.scene.effect.*?>\n'
        + '<?import javafx.geometry.Pos.*?>\n'
        + '<?import javafx.scene.text.*?>\n'
        + '<?import javafx.scene.*?>\n'
        + '<?import javafx.scene.control.*?>\n'
        + '<?import javafx.scene.shape.*?>\n'
        + '<?import javafx.scene.layout.*?>\n'
        + '<?import javafx.collections.*?>\n';


    /**
     * Location of a css file if required.
     * Set this to the appropriate location for the host web-site.
     * The value can be changed from a web-page using
     *       fxdeploy$common.cssFile = file_specifier_string
     */
    var cssFile = '';

    return {

        /**
         * Returns the present header for use in FXML
         * @returns {string}
         */
        getHeader: function () {
            return header;
        },

        /**
         * Sets the header for use in FXML
         * @param string
         */
        setHeader: function (string) {
            header = string;
        },


        /**
         * When using "class=" to identify FXML components from HTML,
         * this method should be called by document onload to embed the
         * JavaFX GUIs.
         *
         */
        init: function () {
            var id;
            var k;
            var callbacks;
            var jnlp;
            var elements = document.getElementsByClassName('fxml-placeholder');
            if (elements.length > 0) {
                var src = elements[0].attributes.src.value;
                id = elements[0].id;
                var appname = elements[0].attributes['data-appname'].value;
                var callbacks = null;
                if (elements[0].attributes['data-callbacks']){
                    callbacks = elements[0].attributes['data-callbacks'].value;
                };
                var jnlp;
                if (elements[0].attributes['data-jnlp']){
                    jnlp = elements[0].attributes['data-jnlp'].value;
                };
                var url = fxdeploy$common.toAbsoluteURL(src);
                fxdeploy$common.loadAsText(url, null, null, elements[0], appname, callbacks, jnlp);
                for (k = 1; k < elements.length; k++) {
                    elements[k].innerHTML = '<p> Only <em>one</em> JavaFX app is supported for each frame</p>';
                }
                return;
            }
            elements = document.getElementsByClassName('fxml-content');
            if (elements.length > 0) {
                var appname = elements[0].attributes['data-appname'].value;
                var callbacks = null;
                if (elements[0].attributes['data-callbacks']){
                    callbacks = elements[0].attributes['data-callbacks'].value;
                };
                var jnlp;
                if (elements[0].attributes['data-jnlp']){
                    jnlp = elements[0].attributes['data-jnlp'].value;
                };
                var scripts = elements[0].getElementsByTagName("script");
                var src = scripts[0];
                var fxml=src.innerHTML;
                id = elements[0].id;
                if (fxml.indexOf('<?xml') < 0) {
                    var idx0=fxml.indexOf('>');
                    var idx1=fxml.indexOf('/>');
                    idx0=Math.min(idx0,idx1);
                    fxml = fxml.substring(0, idx0)
                        .concat(' xmlns="http://javafx.com/javafx/8.0.40" xmlns:fx="http://javafx.com/fxml/1"')
                        .concat(fxml.substring(idx0));
                    fxml = fxdeploy$common.getHeader() + fxml;
                }

                fxdeploy$common.embed(elements[0], fxml, null, appname, callbacks, jnlp);
                for (k = 1; k < elements.length; k++) {
                    elements[k].innerHTML = '<p> Only <em>one</em> JavaFX app is supported for each frame</p>';
                }
                return;
            }
            // No FXML. We may just have a data table so process the JSON and display that with no
            // FXML.
            if (typeof reflector !== "undefined") {
                reflector.embedTableUsingHTML();
            }
        },

        /**
         * Converts a String representing a relative URL to an absolute URL.
         *
         * Specify the relative URL as:
         * <ul>
         *     <li>A path relative to the current document: the input string begins with "."</li>
         *     <li>A path relative to the server address: the input string begins with "/".</li>
         * </ul>
         * If neither of these conditions is met, the input pathname is returned unchanged.
         *
         * @param pathname
         * @returns {String} the absolute path of the resource
         */
        toAbsoluteURL: function (pathname) {
            var regexp = new RegExp('[^/]*\.html');
            if (typeof document === 'undefined')
                return pathname;
            if (pathname.lastIndexOf('/', 0) === 0) {
                pathname = document.location.protocol.concat('//').concat(location.host).concat(pathname);
            } else if (pathname.lastIndexOf('.', 0) === 0) {
                pathname = document.URL.replace(regexp, '').concat(pathname);
                pathname = pathname.replace('/./', '/');
            }
            return pathname;
        },


        /*
         *
         * @param url
         * @param onError
         * @returns the text content as a string
         */
        loadAsText: function (url, onError, onSuccess, element, appName, callbacks, jnlp) {
            var isInBrowser = arguments.length > 3;
            var ready = false;
            var client = new XMLHttpRequest();
            fxdeploy$common.status('Processing JSON');
            client.onreadystatechange = function () {
                if (client.readyState === 4) {
                    if (client.status === 200) {
                        var doc = client.responseText;
                        if (onSuccess !== null) {
                            onSuccess(doc);
                        } else {
                            if (isInBrowser) {
                                if (url.search('.json') > -1
                                    && typeof reflector !== "undefined") {
                                    // This calls into the external Reflector.js code.
                                    // Json is not supported without that, so users must
                                    // supply FXML, not Json or supply their own code
                                    // for the Json -> FXML conversion.
                                    reflector.embedJSON(url, onError, onSuccess, element, appName, doc, callbacks, jnlp);
                                }
                                else {
                                    // FXML
                                    if (doc.indexOf('<?xml version="1.0" encoding="UTF-8"?>') < 0) {
                                        doc = fxdeploy$common.getHeader() + doc;
                                    }
                                    fxdeploy$common.embed(element, doc, null, appName, null, callbacks, jnlp);
                                    // By default, the deployment code will attach JavaScript callbacks
                                    // to all JavaFX items that have an id defined. These shold be unique.
                                    // Given the uniqueness requirement, the ids can be set as fx:ids in the
                                    // FXML file. As long as no explicit id is set, the fx:ids will be copied over
                                    // from the markup as ids available at run-time.
                                    // Users can override this behaviour by
                                    // 1. Clearing the default map: app.getObjectMap().clear();
                                    // 2. Adding the ids and, if required, a value to the map:
                                    //      app.getObjectMap().put('ComboBox$0', 27);
                                    //      app.getObjectMap().put('CheckBox$0', null);
                                    // 3. Then call the initController() method on the app:
                                    //      document.getElementById(appName).initController();
                                    if (app) {
                                        // Initialise the  controller
                                        var app = document.getElementById(appName);
                                        var controllerOK = app.initController();
                                    } else {
                                        alert('Problem adding callbacks: the app is not yet instantiated');
                                    }
                                }
                            }
                        }
                    } else {
                        if (onError !== null) {
                            onError(client.status);
                        } else {
                            alert(client.status);
                        }
                    }
                } else {
                }
            };
            client.open('GET', url, true);
            client.send();
        },


        ///**
        // *
        // * @param element
        // * @param fxml
        // * @param appName
        // */
        //loadFromDOM: function (element, fxml, appName) {
        //    fxdeploy$common.embed(element, fxml, '', appName);
        //},


        /**
         * Embeds the contents of an fxml file in the document at the specified location
         * and launches the app in the page.
         *
         * This includes in-built error handling to replace charts with an HTML text message
         * when Java/JavaFX is not supported.
         *
         * Specify the path to the FXML file as:
         * <ul>
         *     <li>A full url e.g. "http://....."</li>
         *     <li>A path relative to the current HTML document if the string begins with "."</li>
         *     <li>A path relative to the server address if the string begins with "/".</li>
         *     <li>The name of a file packed within the jnlp file.</li>
         * </ul>
         *
         * @param element
         * @param fxmlText
         * @param pathToCss
         * @param appName
         * @param data
         */
        embed: function (element, fxmlText, pathToCss, appName, data, callbacks, jnlp) {

            fxdeploy$common.status('Embedding GUI');

            // User-defined callbacks for the JavaFX nodes.
            // If these
            var jscallbacks = null;
            if (arguments.length == 6 && callbacks) {
                jscallbacks = callbacks;
            } else {
                if (typeof fxml$callbacks !== "undefined") {
                    jscallbacks = fxml$callbacks;
                } else if (typeof reflector$callbacks !== "undefined") {
                    jscallbacks = reflector$callbacks;
                }
            }

            // If the JNLP file is not defined on input, use the
            // default setting in the jnlp_file
            if (!jnlp){
                jnlp=default_jnlp_file;
            }

            if (arguments.length < 3 && !pathToCss) {
                pathToCss = this.cssFile;
            }
            var w = 0;
            var h = 0;
            if (element && element.style) {
                w = 700;
                w = element.style.width;
                h = element.style.height;
            }
            if (w <= 0) w = 560;
            if (h <= 0) h = 420;

            dtjava.embed(
                {
                    id: appName,
                    url: jnlp,
                    //jnlp_content: jnlp_base64,
                    placeholder: element,
                    width: w,
                    height: h,
                    params: {
                        fxml: fxmlText,
                        data: (arguments.length >=5) ? JSON.stringify(data): null,
                        callbacks: jscallbacks
                        //css: toAbsoluteURL(pathToCss)
                    }
                },
                {
                    javafx: '8.0+'
                },
                {
                    onDeployError: function (app, r) {
                        if (app != null) {
                            self.location = 'http://undocumentedmatlab.com/AppReflector/Demos/Content/SystemTest/index.html';
                        } else {
                            //use default handlers otherwise
                            var def = new dtjava.Callbacks();
                            return def.onDeployError(app, r);
                        }
                    }
                }
            );
        },

        /**
         * Updates an HTML element with id='reflector:status' in the present
         * document (if it exists).
         * Typically the contents will be a simple message string, but complex
         * HTML can also be used.
         *
         * @param string to be placed in the element.
         */
        status: function (string) {
            var message_element;
            // N.B. Backwards compatibility only: use 'reflector-status'
            message_element = document.getElementById('reflector:status');
            if (!message_element) {
                message_element = document.getElementById('reflector-status');
            }
            if (!message_element) {
                message_element = document.getElementById('fxml-status');
            }
            if (message_element) {
                message_element.innerHTML = string;
            }
        }
    };

}();

if (typeof define !== 'undefined') {
    define(function (require) {
        return fxdeploy$common;
    });
}