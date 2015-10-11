/**
 * <p>
 * Copyright (c) 2015, Malcolm Lidierth, UK. All rights reserved.
 *
 * Modifications for use with AppReflector etc.: Copyright (c) 2015,
 * AppReflector Ltd, UK. All rights reserved.
 * </p>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.appreflector.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import netscape.javascript.JSObject;


/**
 * FXWebApp is used to embed a JavaFX scene into a browser and support user
 * interaction via JavaScript callbacks in the host environment.
 *
 * <p>
 * FXWebApp is likely to be especially useful to web developers who want to use
 * JavaFX in their web pages, but avoid writing/compiling their own Java code.
 * </p>
 *
 * <p>
 * An FXWebApp application is embedded in a web page using JavaScript.
 * </p>
 *
 * <p>
 * FXWebApp supplements the code in the standard Java Web Deployment Toolkit as
 * follows:
 * <ul>
 * <li><strong>Arbitrary JavaFX scenes can be embedded</strong> in a web page by
 * loading a user-designed GUI specified in FXML - the Oracle defined markup
 * language for JavaFX. <br> Accompanying JavaScript code allows the FXML to be supplied as a
 * file, or in-lined within the hosting HTML document (see below)</li>
 * <li><strong>Web developers can create controllers for their GUIs by coding in
 * JavaScript</strong> - there is no need to do further coding in Java. This is
 * achieved by attaching the supplied default controller (FXWebAppController:
 * written in Java). Listeners within this controller automatically forward
 * events to user-written JavaScript callbacks within the host context.
 * </li>
 * <li><strong>Convenience methods are provided to allow users to run JavaScript
 * code from the JavaFX Platform thread</strong> - these methods ensure
 * thread-safety.
 * </li>
 * </ul>
 * <p>
 * <strong>FXML</strong><br>
 * An extremely polished and versatile IDE for designing JavaFX-based GUIs that
 * creates FXML files as output is freely available from
 * <a href="http://gluonhq.com/open-source/scene-builder/">http://gluonhq.com/open-source/scene-builder/</a>.
 * As FXML can be deployed to desktop, web and mobile devices, its use here has
 * the advantage that the same GUI-code can target each environment. Java
 * programmers intending to do that may prefer to add code to the
 * {@code FXWebAppController} listeners (or replace it with a custom controller)
 * instead of writing JavaScript callbacks to make porting the code easier.
 * <p>
 * <strong>USE</strong><br> {@code FXWebApp} is intended to work alongside the
 * dtjava.js code supplied as part of the Java web deployment toolkit.<br>
 * A closed source copy is available from
 * <a href="http://java.com/js/dtjava.js">Oracle</a><br>
 * and an open-source copy (GNU GPL v2) as part of the OpenJDK project
 * <a href="http://hg.openjdk.java.net/openjfx/8/master/rt/file/tip/modules/fxpackager/src/main/resources/resources/web-files/dtjava.js">from
 * here</a>. See Note [1] below.
 * <br><br>
 * <em><strong>Direct use of dtjava.js</strong></em><br>
 * If the dtjava.js {@code embed} method is called directly, the following
 * parameters can be supplied and will be forwarded to the
 * {@code FXWebApp start} method:
 * <br> {@code dtjava.embed( }{<br> {@code id: appName,}<br>
 * {@code url: jnlp,}<br> {@code placeholder: element,}<br>
 * {@code width: w,}<br> {@code height: h,}<br> {@code params: }{
 * {@code fxml: fxmlText,}<br> 
 * {@code data: '',}<br> {@code callbacks: callbacks}}});<br>
 *  <br>
 * Details are as follows:<br>
 * <ul>
 * <li><strong>id: appName</strong> - the id to be assigned to the embedded
 * application</li>
 * <li><strong>url: jnlp</strong> - the fully qualified path and file name to
 * this project's JNLP file</li>
 * <li><strong>placeholder: element</strong> - reference to the host element in
 * the HTML file where graphics will be embedded</li>
 * <li><strong>width: w</strong> - width of the element</li>
 * <li><strong>height: h</strong> - height of the element</li>
 * </ul><br>
 * The <strong>params</strong> field provides data for use in the {@code FXWebApp start}
 * methods. These are:
 * <ul>
 * <li><strong>fxml</strong> - the FXML to parse as a string</li>
 * <li><strong>data</strong> - a string containing data (typically as Json) that
 * will be passed to the callbacks. Once the application is embedded, this
 * string can be changed by calling the {@code setData} method on the application.</li>
 * <li><strong>callbacks</strong> - the name of the JavaScript object in the
 * host context containing the callbacks (if any).</li>
 * </ul>
 * <br>
 * <em><strong>Use with the accompanying JavaScript code</strong></em>
 * <p>
 * An accompanying JavaScript file, <strong>fxdeploy-common.js</strong>,
 * provides code for specifying use of an {@code FXWebApp} from HTML
 * with minimal JavaScript coding.
 * <br>
 * 
 * To use fxdeploy-common.js, add the following to your HTML header element<br>
 * {@code <head>}<br>
 * {@code ...}<br>
 * {@code <script src="http://java.com/js/dtjava.js"></script>}<br>
 * {@code <script src="fxdeploy-common.js"></script>}**<br> {@code ...}<br>
 * {@code </head>}<br>
 * ** this declaration here should include the full path information to the
 * JavaScript file.
 * <p>
 *
 * <p>
 * Once loaded, fxdeploy-common.js will create a variable,
 * fxdeploy$common that defines a set of functions. Only one of these is usually of
 * interest:<br> {@code fxdeploy$common.init()}<br>
 * should be called once your HTML document loads. E.g. add the following in
 * your HTML file:<br> {@code <body onload="fxdeploy$common.init()">}<br>
 * This function scans the HTML document for an element marked up as described
 * below and automatically loads the FXML content into it.
 * </p>
 * <ul>
 * <li>
 * <em><strong>Creating HTML document for fxdeploy-common.js</strong></em>
 * <p>
 * The JavaScript code recognises HTML elements marked up with the following
 * classnames:
 * </p>
 * <ul>
 * <li><strong>fxml-placeholder</strong> - an element that will be filled with
 * JavaFX contents described in an FXML document (file).</li>
 * <li><strong>fxml-content</strong> - an element containing in-line FXML. This
 * will be read and parsed. The JavaFX contents will be generated and replace
 * the in-line FXML.</li>
 * <li><strong>fxml-status</strong> - an element that will be updated, if
 * present, with progress information as the FXML loads and is processed by the
 * code.</li>
 * </ul>
 *
 * <p>
 * <em>
 * Note that only one {@code fxml-placeholder} or {@code fxml-content} element
 * is permitted per HTML frame. Also, only one {@code fxml-status} element is
 * supported per frame.
 * </em>
 *
 * <ul>
 * <li><strong>fxml-placeholder</strong>
 * <p>
 * Placeholders are declared as follows:
 * <p>
 * {@code <div class="fxml-placeholder" src='myGUI.fxml' data-appname="myApp" data-callbacks="myCallbacks" data-jnlp="./FXDeploy.jnlp">}<br>
 * {@code ...}<br> {@code </div>}
 * </p>
 * <ul>
 * <li><strong>src</strong> - provides the full path and file name of the FXML
 * file</li>
 * <li><strong>data-appname</strong> - provides a name for the embedded Java
 * application.
 * <br>
 * The application may be accessed, once loaded, by calling to
 * document.getElementById(data-appname);
 * </li>
 * <li><strong>data-callbacks</strong> - a string providing the name for the
 * variable containing the JavaScript callbacks for the JavaFX node.<br>
 * If data-callbacks is not defined, a default value will be assigned. Either:
 * <ol>
 * <li>fxml$callbacks OR</li>
 * <li>reflector$callbacks</li>
 * </ol>
 * if one of these variables is defined in the calling workspace.<br>
 * The Java listeners will automatically invoke these callbacks as described
 * below.
 * </li>
 * <li><strong>data-jnlp</strong> provides the fully qualified path and file
 * name for the JNLP file containing this Java code.<br>
 * Note that fxdeploy-common.js defines a variable close to its start that
 * provides a default value for this. If the data-jnlp is omitted from your
 * HTML, the default value will be used:<br>
 * {@code var jnlp_file = '../../../java/FXDeploy/dist/FXDeploy.jnlp';}<br>
 * This path here needs to be edited to point to a copy of the JNLP file local
 * to your web resource.<br><br>
 * </ul>
 *
 * <li><strong>fxml-content</strong><br>
 * To inline the FXML code within an HTML document use<br>
 * {@code <div class="fxml-content" data-appname="myApp" data-callbacks="myCallbacks" data-jnlp="./FXDeploy.jnlp">}<br>
 * {@code <script type="text/plain">}<br> {@code ...}<br> {@code </script>}<br>
 * {@code </div>}<br>
 * For definitions of the "data-" properties, see the fxml-placeholder entry
 * above.<br>
 * The content of the {@code <script>} block should be the FXML.
 * <ul>
 * <li><em><strong>Short-form FXML</strong></em><br>
 * If the header information is absent from the FXML, it will be added
 * automatically. Thus you can specify FXML as below:<br>
 * {@code <script type="text/plain">}<br>
 * {@code   <Pane  prefHeight="400.0" prefWidth="600.0">}<br>
 * {@code       <children>}<br>
 * {@code           <Button fx:id="myButton" layoutX="271.0" layoutY="174.0" text="Hello World!" />}<br>
 * {@code       </children>}<br> {@code   </Pane>}<br> {@code  </script>}<br><br>
 * The FXML that is created will be as follows (subject to updates - check the
 * code):<br><br> {@code <?xml version="1.0" encoding="UTF-8"?>}<br>
 * {@code <?import java.lang.*?>}<br> {@code <?import java.util.*?>}<br>
 * {@code <?import javafx.scene.effect.*?>}<br>
 * {@code <?import javafx.geometry.Pos.*?>}<br>
 * {@code <?import javafx.scene.text.*?>}<br>
 * {@code <?import javafx.scene.*?>}<br>
 * {@code <?import javafx.scene.control.*?>}<br>
 * {@code <?import javafx.scene.shape.*?>}<br>
 * {@code <?import javafx.scene.layout.*?>}<br>
 * {@code <?import javafx.collections.*?>}<br>
 * <br>
 * {@code <Pane  prefHeight="400.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/8.0.40" xmlns:fx="http://javafx.com/fxml/1">}<br>
 * {@code  <children>}<br>
 * {@code  <Button fx:id="myButton" layoutX="271.0" layoutY="174.0" text="Hello World!" />}<br>
 * {@code   </children>}<br> {@code  </Pane>}<br><br>
 * </li>
 * <li><em><strong>Fully-formed FXML</strong></em><br>
 * If the inlined {@code <script>} starts with "{@code <?xml"} it will be used
 * as supplied. Users may therefore customise imports, namespaces etc.<br>
 * Note that "xmnls" and "xmlns:fx" may be required and need to be specified by
 * the user in this case.
 * </ul>
 * </li>
 * </ul>
 *
 * </ul>
 * <br>
 * <em><strong>CALLBACKS</strong></em><br>
 * The {@code FXWebApp start} attaches a controller to the
 * {@code FXWebApp} instance. Once initialised, listeners in this controller
 * will forward events to user-supplied JavaScript callbacks.<br>
 * Users populate the callbacks with their own JavaScript code. For example:<br>
 * {@code fxmlCallbacks: }{<br>
 * {@code myButton_callback: function(...) }{<br>
 * {@code ...}}}<br>
 * In this case, "fxmlCallbacks" would be supplied as the "callbacks" element
 * in the "params" field of the {@code dtjava.embed} call or set as the
 * data-callbacks attribute in HTML when using fxdeploy-common.js.<br>
 * 
 * <p>
 * <strong>
 * When not using fxdeploy-common.js, initialisation of the controller that
 * invokes the callbacks needs to be done explicitly from the calling JavaScript
 * once the application is embedded.<br>
 * </strong></p>
 * <p>
 * <strong><em>The default {@code FXWebAppController}</em></strong>
 * </p>
 * <p>
 * The description below is for the default {@code FXWebAppController} added in
 * the {@code start} method. Java programmers can supply their own controller.
 * <p>
 * If the object containing the callbacks was specified as a parameter to the
 * {@code start} method simply call {@code initController()} e.g.<br>
 * {@code document.getElemenyById(appName).initController()};<br>
 * Otherwise, specify the callback object at initialisation:<br>
 * {@code document.getElemenyById(appName).initController(callbackObject)};
 * </p>
 * <p>
 * {@code FXWebApp} maintains a {@code LinkedHashMap<String, Object>} that
 * lists the nodes to which listeners/callbacks should be attached. The keys of
 * this {@code HashMap} should correspond to the ids (returned by calling
 * {@code getId()}) of nodes that descend from the root object defined in the
 * FXML supplied to the {@code start} method.<br>
 * If this map is left empty, it will be populated automatically during
 * initialisation with the ids of <strong>all</strong> nodes that have a defined
 * id. In this case, the values corresponding to each key in the
 * {@code LinkedHashMap<String, Object>} will be {@code null}.<br>
 * Alternatively, users can populate the map from javaScript <em>before</em>
 * initialising the controller. In this case, the values in the map can be
 * assigned by the user - and these values will be passed to the JavaScript
 * callbacks (as described below). To populate the map from JavaScript use code
 * such as the following:<br><br>
 * {@code var map = document.getElemenyById(appName).getCallbackObjectMap();}<br>
 * {@code map.clear();}<br> {@code map.put('myButton', 'user data');}<br>
 * {@code map.put('myCheckBox', JSON.stringify(userData));}<br><br>
 * </p>
 * <p>
 *
 * <em><strong>Setting node ids</strong></em><br>
 * The ids of each node can be set in the FXML by:<br>
 * <ol>
 * <li>Setting the fx:id property (which ensures uniqueness when using an IDE).
 * When the FXML is processed, the fx:id will be copied to the id as long as no
 * explicit id is set for the node.</li>
 * <li>Setting the id property directly, in which case the fx:id may or may not
 * also be used.</li>
 * </ol>
 * <p>
 * <em><strong>Attaching callbacks</strong></em><br>
 * For each node in the {@code LinkedHashMap<String, Object>}, a Java listener
 * is attached during initialisation. This listener passes events to the
 * JavaScript callbacks as follows:
 * <ol>
 * <li> If a function is defined in the callback parent object that has a name
 * equivalent to the nodes's id appended with "_callback", that function will be
 * called.</li>
 * <li> If not, if a function is defined in the callback parent object that has
 * a name equivalent to node's shortened, lower case class name appended with
 * "$callback", that function will be called.</li>
 * </ol><br>
 * The callback parent object might then look as follows:<br>
 * {@code fxml$callbacks = }{<br>
 * {@code myButton_callback: function (...)} {<br> {@code ...}<br>
 * },<br> {@code checkbox$callback: function (...)}{<br> {@code ...}<br>
 * }<br> }
 * <p>
 * <strong>The default controller always runs JavaScript callbacks the JavaFX Platform
 * thread.</strong>
 * </p>
 * Note that:
 * <ol>
 * <li> If objects share an id, they will also share a callback.</li>
 * <li> If callbacks are based on the class name, they will be shared by all
 * objects of that class lacking an id.</li>
 * </ol>
 * <p>
 * <em><strong>Callback arguments</strong></em><br>
 * The following are passed as arguments to the callback functions:
 * <ol>
 * <li> A reference to the node that generated the callback</li>
 * <li> A reference to the event that generated the callback</li>
 * <li> The value associated with this node's key in the
 * {@code LinkedHashMap<String, Object>}</li>
 * <li> The data string that was supplied as a parameter to the {@code start}
 * method or by a subsequent call the {@code FXWebApp setData} method.</li>
 * </ol>
 * When callbacks are shared because two or more nodes share an id the third
 * argument will be common to each of those objects.
 * <p>
 * <em><strong>Multiple calls to {@code initController}</strong></em><br>
 * Although it is anticipated that the initController method will generally be
 * called only once, it may be called repeatedly to update/change callbacks.
 * Each call to initController:
 * <ol>
 * <li> Installs a Java listener/JavaScript callback pair if none presently
 * exists</li>
 * <li> Replaces the existing listener/callback pair if they do exist</li>
 * </ol>
 *
 * <p>
 * <em><strong>CREATING WINDOWS EXTERNAL TO THE HTML DOCUMENT</strong></em><br>
 * An {@code FXWebApp} instance is always embedded in the hosting HTML frame.
 * The {@code FXWebApp createWindow} method allows an embedded {@code FXWebApp}
 * instance to create new JavaFX windows external to the hosting frame e.g. to
 * display supplementary information when a button in the main document is
 * clicked.
 * <p>
 * To create such a window call the the {@code createWindow} method on an
 * existing application embedded as above:<br>
 * {@code var myWindow = getElementById(appName).createWindow(fxml, data, callbacks)};<br>
 * This returns an {@code FXWebWindow} instance in "myWindow". This class
 * subclasses {@code FXWebApp} and supports all methods associated with it. The
 * arguments to the {@code createWindow} method define the FXML, data and
 * callback object to be associated with the {@code FXWebWindow} similarly to
 * when creating an {@code FXWebApp} instance.
 * <ul>
 * <li><em><strong>fxml</strong></em> is a string containing the FXML. It may be
 * short-form or fully-formed FXML as described above for fxml-content elements
 * within an HTML document. If FXML is supplied from file, this should be loaded
 * as a string first using JavaScript.</li>
 * <li><em><strong>data</strong></em> a string containing data that will be
 * passed to any callbacks
 * <li><em><strong>callbacks</strong></em> a string naming the JavaScript
 * variable in the host context that defines the callbacks.
 * </ul>
 * <p>
 * {@code FXWebWindow} instances are "owned" by the {@code FXWebApp} instance
 * that created them. A list of owned {@code FXWebWindow}s for an embedded
 * application can be retrieved with:<br>
 * {@code getElementById(appName).getOwnedWindows()}<br>
 * which returns an ArrayList.
 * </p>
 *
 * <p>
 * <em><strong>HELPER METHODS EXPOSED TO JAVASCRIPT FROM {@code FXWebApp}</strong></em><br>
 * {@code FXWebApp} uses the
 * highest node in the hierarchy of the user-supplied FXML code as the reference
 * point, or root, for its code. The root is always a JavaFX {@code Pane} or
 * subclass of {@code Pane}.<br>
 *
 * The root node is added to a JavaFX {@code Scene} which is then added to a
 * {@code Stage} when the application is first embedded.<br>
 *
 * The {@code getScene} and {@code getStage} methods search <em>from the root
 * node</em>. If the root is moved to a new, user-created, {@code Scene} or
 * {@code Stage} these methods will return the <em>current</em> {@code Scene} or
 * {@code Stage}.<br>
 *
 * Nodes in the hierarchy can be located by using the JavaFX {@code lookup} and
 * {@code lookupAll} methods which behave rather like JavaScript
 * {@code getElementById} and {@code getElementByClassName} methods. For
 * example:<br><br>
 * {@code var button = document.getElementById(appName).lookup(#myButton)}<br><br>
 * will return a reference to the JavaFX node with an id of "myButton" while
 * <br><br>
 * {@code var node = document.getElementById(appName).lookupAll(".className")}<br><br>
 * will return a {@code Set<Node>} listing all nodes with the specified CSS
 * class name.<br><br>
 * {@code lookup} is shorthand for {@code getRoot().lookup}
 * while {{@code lookupAll} is short for {@code getRoot().lookupAll}. To search
 * from the {@code Scene}, use {@code getScene().lookup}.<br>
 *
 * Java methods my be invoked from JavaScript on these references e.g.:<br><br>
 * {@code var button = document.getElementById(appName).lookup(#myButton);}<br>
 * {@code myButton.setText("My new label");}<br><br>
 * However, users should take care to ensure that code is run on the JavaFX
 * Platform thread when accessing JavaFX nodes. The {@code FXWebApp} class
 * provides methods to make this easier:<br><br>
 * {@code var app = document.getElementById(appName);}<br>
 * {@code app.runFX(function()}{<br>
 * {@code var myButton = app.lookup('#myButton');}<br>
 * {@code myButton.setText("My new label");}<br> }) ;<br><br>
 * An alternative form of this call allows arguments to be supplied:<br><br>
 * {@code var app = document.getElementById(appName);}<br>
 * {@code app.runFX(function(args)}{<br>
 * {@code var myButton = app.lookup(args[0);}<br>
 * {@code myButton.setText(args[1);}<br> },
 * {@code ["#myButton", "My new label"]);}<br><br>
 * <em>Note the use of square brackets on the last line. These are required even
 * when there is only one argument.</em>
 * </p>
 * <p>
 * The {@code runFX} methods post a request for the JavaScript function to be
 * called from the Platform thread queue. The methods do not block while the
 * function is run and can not return a result.<br>
 * If you need to return a result, equivalent blocking methods are supplied:
 * <ul>
 * <li><strong>runAndWaitFX</strong> blocks and returns the result when the
 * callback completes</li>
 * <li><strong>runAndGoFX</strong> does not block and returns a
 * {@code FutureTask} instance. Progress can be monitored in JavaScript by
 * calling the {@code isDone} method on this object. Calling its {@code get}
 * method will block until {@code isDone} is true and will return the result
 * from the callback method.<br>
 * <em>
 * The {@code FutureTask} instance is also passed as an input to the callback,
 * which may therefore call the {@code isCancelled} function and terminate
 * callback execution as required.
 * </em>
 * </li>
 * </ul>
 * <br>
 * Trivial examples are shown below:<br> {@code runFXAndWait}:<br><br>
 * {@code var app = document.getElementById(appName);}<br>
 * {@code var newLabel = app.runAndWaitFX(function()}{<br>
 * {@code var myButton = app.lookup('#myButton');}<br>
 * {@code myButton.setText("My new label");}<br>
 * {@code return myButton.getText();}<br> }) ;<br><br>
 * {@code var app = document.getElementById(appName);}<br>
 * {@code var newLabel = app.runAndWaitFX(function(args)}{<br>
 * {@code var myButton = app.lookup(args[0]);}<br>
 * {@code myButton.setText(args[1]);}<br> {@code return myButton.getText();}<br>
 * }, {@code ["#myButton", "My new label"]);}<br><br>
 * {@code runFXAndGo}:<br><br>
 * {@code var app = document.getElementById(appName);}<br>
 * {@code var task = app.runAndGoFX(function(future)}{<br>
 * {@code var myButton = app.lookup('#myButton');}<br>
 * {@code if (future.isCancelled()) return null;}<br>
 * {@code myButton.setText("My new label");}<br>
 * {@code return myButton.getText();}<br> }) ;<br> {@code ...}// User-supplied
 * JavaScript continues running here<br> {@code var result=task.get()}// This
 * blocks until the task completes (or is canceled) and returns the
 * result<br><br> {@code var app = document.getElementById(appName);}<br>
 * {@code var task = app.runAndGoFX(function(args, future)}{<br>
 * {@code var myButton = app.lookup(args[0]);}<br>
 * {@code if (future.isCancelled()) return null;}<br>
 * {@code myButton.setText(args[1]);}<br> {@code return myButton.getText();}<br>
 * }, {@code ["#myButton", "My new label"]);}<br> {@code ...}// User-supplied
 * JavaScript continues running here<br> {@code var result=task.get()}// This
 * blocks until the task completes (or is canceled) and returns the
 * result<br><br>
 *
 * <em><strong>Use {@code runFX}, {@code runAndWaitFX} and {@code runAndGoFX}
 * methods only when needed as they use the JavaFX Platform thread. Potentially
 * lengthy operations such as file I/O or HTTP requests should be avoided in
 * these functions. This is particularly so with {@code runAndWaitFX} which
 * blocks until the supplied function completes.</strong></em>
 * 
 * <ul>
 * <li><strong>Adding more helper functions</strong><br>
 * The {@code HostUtilitiesInterface} defines an interface that implements
 * by custom Java classes to provide additional helper functions.
 * <br>
 * A host utilities instance can be retrieved by call the {@code getUtils} method
 * of the application. It can be set by calling {@code setUtils("className")}.
 * <br>
 * Custom host utility classes can therefore be added to the Java code if it is
 * recompiled. The following assumptions are made, EITHER:
 * <ol>
 * <li>The utilities class has a no-argument constructor and implements only static
 * methods</li>
 * <li>The utilities class has a constructor which takes the {@code FXWebApp} instance
 * as input and can therefore provide provides that need a reference to the application
 * as well as static methods.
 * </li>
 * </ol>
 * </li>
 * </ul>
 *
 * <p>
 * <strong>Notes: </strong><br>
 * <ol>
 * <li>Both Oracle and OpenJDK versions of dtjava.js have some issues: {1} Synchronous
 * script loading is assumed. [2} Chrome is not supported on Mac. A revised
 * version of the open-source dtjava.js that addresses these issues is bundled with this code.
 * Also note that Java support, which uses NPAPI, will be discontinued in Chrome
 * as of Chrome release 43.</li>
 * </ol>
 *
 * @author ML
 */
public class FXWebApp extends Application {

    private Pane root;

    /**
     * The app name for use within the host context {@code e.g.} in JavaScript,
     * use {@code document.getElementById(appName)}.
     */
    //private String appName;
    private String fxml;
    private FXWebAppControllerInterface controller;
    private String callbacks = null;
    private String data;
    private final LinkedHashMap<String, Object> callbackObjectMap = new LinkedHashMap();

    /**
     * A list of {@code FXWebWindows} owned by this {@code FXWebApp} instance.
     *
     */
    private final ArrayList<FXWebWindow> ownedWindows = new ArrayList<>();

    /**
     * This contains a reference to a user-defined class that implements the
     * {@code HostUtilitiesInterface} and provides Java methods for use from the
     * host. These will typically be called via JavaScript.
     */
    private HostUtilitiesInterface hostUtilities;

    /**
     * This is the default header that will be applied <em>only</em> when the
     * supplied FXML lacks one.
     *
     */
    private static final String defaultFxmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "\n"
            + "<?import java.lang.*?>\n"
            + "<?import java.util.*?>\n"
            + "<?import javafx.scene.effect.*?>"
            + "<?import javafx.geometry.Pos.*?>"
            + "<?import javafx.scene.text.*?>\n"
            + "<?import javafx.scene.*?>\n"
            + "<?import javafx.scene.control.*?>\n"
            + "<?import javafx.scene.shape.*?>\n"
            + "<?import javafx.scene.layout.*?>"
            + "<?import javafx.collections.*?>";

    @Override
    public void start(Stage primaryStage) {

        //hostUtilities = new HostUtilities(this);
        Scene scene;

        // The FXML to embed as a String
        String s = getParameters().getNamed().get("fxml");

        if (s == null || s.isEmpty()) {
            Text text = new Text("The calling JavaScript has not provided the parameters needed by FXWebApp");
            root = new BorderPane(text);
            root.setPrefSize(400, 300);
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            return;
        }

        // Data object
        data = getParameters().getNamed().get("data");

        // Name of the host context member defining any JavaScript callbacks
        // to invoke from the controller
        callbacks = getParameters().getNamed().get("callbacks");

        // Set the width and height
        double w;
        double h;
        try {
            w = Double.parseDouble(getParameters().getNamed().get("w"));
        } catch (Exception ex) {
            w = 400;
        }
        try {
            h = Double.parseDouble(getParameters().getNamed().get("h"));
        } catch (Exception ex) {
            h = 300;
        }

        // Start up display
        ProgressIndicator progress = new ProgressIndicator(-1d);
        progress.setMaxSize(w / 5d, h / 5d);
        root = new BorderPane(progress);
        root.setPrefSize(w, h);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add the default header if one has not been provided
        if (!s.startsWith("<?xml")) {
            s = defaultFxmlHeader.concat(s);
        }
        fxml = s;
        FXMLLoader loader = new FXMLLoader();

        controller = new FXWebAppController();
        loader.setController(controller);

        InputStream stream = new ByteArrayInputStream(fxml.getBytes());
        try {
            root = loader.load(stream);
        } catch (IOException ex) {
            root.getChildren().add(new Text("FXWebApp.java encountered and error: " + ex.getMessage()));
        }

        scene.setRoot(root);

        root.requestLayout();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            ownedWindows.stream().forEach((FXWebWindow window) -> {
                if (window.getStage().isShowing()) {
                    window.getStage().close();
                }
            });
        });

    }

    /**
     * Method to initialise a {@code FXWebAppController} instance associated
     * with this {@code FXWebApp} instance.
     *
     * The controller is instantiated, but not initialised, in the
     * {@code start()} method.
     *
     * This method requires the callbacks JS member to have been defined when
     * the {@code start()} method was called.
     *
     * Conditions required to install callbacks are that the callbacks property
     * of this instance are neither null nor empty, and also that the host
     * context is defined. If the host context is not defined, the callbacks
     * property will be set to null, allowing it to be set subsequently through
     * a call to {@code initController(callbacks)} variant of this method.
     *
     * @return true if the conditions required to install the callbacks are
     * satisfied - false otherwise.
     */
    public boolean initController() {
        if (callbacks != null && !callbacks.isEmpty() && getHostContext() != null) {
            Platform.runLater(() -> {
                controller.initialize(this, callbacks);
            });
            return true;
        } else {
            callbacks = null;
            return false;
        }
    }

    /**
     * Method to initialise a {@code FXWebAppController} instance associated
     * with this {@code FXWebApp} instance.
     *
     * The controller is instantiated, but not initialised, in the
     * {@code start()} method.
     *
     * This method allows the callbacks JS member to be defined on-the-fly
     * instead of in the {@code start()} method.
     *
     * Conditions required to install callbacks are that the callbacks argument
     * is neither null nor empty, and also that the host context is defined. If
     * the host context is not defined, the callbacks property of this instance
     * will be set to null, allowing it to be set subsequently through a further
     * call to {@code initController(callbacks)}.
     *
     * @param callbacks name of the host context variable containing the callbacks.
     *
     * @return true if the conditions required to install the callbacks are
     * satisfied - false otherwise.
     */
    public boolean initController(String callbacks) {
        if (this.callbacks == null) {
            this.callbacks = callbacks;
            return initController();
        } else {
            return false;
        }
    }

    /**
     * Returns the name of the host context object containing the callbacks.
     *
     * @return the host context object name.
     */
    public String getCallbacks() {
        return callbacks;
    }

    /**
     * Returns the root node associated with this {@code FXWebApp} instance.
     *
     * The root is the top level container defined in the FXML file provided to
     * the {@code start()} method.
     *
     * @return the root node
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Returns the scene presently associated with the root of this
     * {@code FXWebApp} instance.
     *
     * The {@code Scene} is found by calling {@code getScene} on the root
     * object.
     *
     *
     * @return the Scene
     */
    public Scene getScene() {
        return root.getScene();
    }

    /**
     * Returns the window presently associated with the root of this
     * {@code FXWebApp} instance.
     *
     * The {@code Window} is found by calling {@code getStage} on the
     * {@code Scene} returned by the {@code gatScene} method.
     *
     *
     * @return the window
     */
    public Stage getStage() {
        return (Stage) getScene().getWindow();
    }

    /**
     * Returns the fxml used to create this root.
     *
     * Used primarily for debugging. The return fxml is that provided to the
     * FXML loader, including any changes that may have been made in the
     * {@code start()} method (such as adding headers).
     *
     * @return the fxml String
     */
    public String getFxml() {
        return fxml;
    }

    /**
     * @deprecated
     */
    public LinkedHashMap<String, Object> getObjectMap() {
        return getCallbackObjectMap();
    }

    /**
     * Returns a {@code LinkedHashMap<String, Object>} specifying those objects
     * that have callbacks associated with them.
     *
     * The keys for the map are the {@code String} ids returned by the
     * {@code getId()} method called on the underlying JavaFX objects. Note that
     * this requires the ids to be unique.
     *
     * The values associated with each key are user-specified objects that may
     * be passed as arguments to the JavaScript callback associated with the
     * JavaFX object via the FXWebAppControllerInterface implementation.
     *
     * Note, that this is implementation specific: the
     * FXWebAppControllerInterface does not require the callbackObjectMap to be
     * supported.
     *
     * @return the map.
     */
    public LinkedHashMap<String, Object> getCallbackObjectMap() {
        return callbackObjectMap;
    }

    /**
     * @return the hostUtilities instance
     */
    public HostUtilitiesInterface getUtil() {
        return hostUtilities;
    }

    private void setUtil(HostUtilitiesInterface instance) {
        this.hostUtilities = instance;
    }

    public String setUtil(String name) {
        if (!name.contains(".")) {
            name = "com.appreflector.common.".concat(name);
        }
        Class clzz;
        try {
            clzz = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            return ex.toString();
        }
        Constructor[] constructors = clzz.getDeclaredConstructors();
        if (constructors.length > 0) {
            int nparam = constructors[0].getParameterCount();
            Object instance;
            if (nparam == 1) {
                try {
                    instance = constructors[0].newInstance(this);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    return ex.toString();
                }
            } else {
                try {
                    instance = clzz.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    return ex.toString();
                }
            }
            if (instance instanceof HostUtilitiesInterface) {
                setUtil((HostUtilitiesInterface) instance);
            } else {
                return "Not a valid HostUtilitiesInterface";
            }
            return "OK";
        }
        return "No constructors found ?!!";
    }

    /**
     * Returns a user-specified data {@code String}.
     *
     * Typically, this String will be JSON and allows data to be specified in
     * this FXWebApp instance that can be passed to the callbacks invoked
     * through the FXWebAppControllerInterface.
     *
     * Note, that this is implementation specific: the
     * FXWebAppControllerInterface does not require the callbackObjectMap to be
     * supported.
     *
     * @return the data as a String
     */
    public String getData() {
        return data;
    }

    /**
     * Sets a user-specified data {@code String}.
     *
     * Typically, this String will be JSON and allows data to be specified in
     * this FXWebApp instance that can be passed to the callbacks invoked
     * through the FXWebAppControllerInterface.
     *
     * Note, that this is implementation specific: the
     * FXWebAppControllerInterface does not require the callbackObjectMap to be
     * supported.
     *
     * @param data the data String
     */
    public void setData(String data) {
        this.data = data;
    }

//    public String getAppName() {
//        return appName;
//    }
    public JSObject getHostContext() {
        return getHostServices().getWebContext();
    }

    public FXWebAppControllerInterface getController() {
        return controller;
    }

    /**
     * Tests if the code is running on the JavaFX Platform thread.
     *
     * @return true if the method is run on the Platform thread, false
     * otherwise.
     */
    public boolean isFxApplicationThread() {
        return Platform.isFxApplicationThread();
    }

    /**
     * Runs supplied callback will on the Platform thread.
     *
     * @param callback the code to run
     */
    public static void runFX(JSObject callback) {
        runFX(callback, null);
    }

    /**
     * Runs supplied callback will on the Platform thread.
     *
     * @param callback the code to run
     * @param data a data object to be passed to the callback as input
     */
    public static void runFX(JSObject callback, JSObject data) {
        Object[] args = new Object[2];
        args[0] = null;
        args[1] = data;
        Platform.runLater(() -> {
            callback.call("call", args);
        });
    }

    /**
     * Used here to simulate a Platform.runAndWait which JavaFX Platform does
     * not provide. The supplied callback will run on the Platform thread, and
     * the method will block until the callback has completed.
     *
     * @param callback the code to run
     * @return the output from the code
     * @throws java.lang.InterruptedException if the code is interrupted
     * @throws ExecutionException if a an exception occurs during execution
     */
    public static Object runAndWaitFX(JSObject callback) throws InterruptedException, ExecutionException {
        return runAndWaitFX(callback, null);
    }

    /**
     * Used here to simulate a Platform.runAndWait which JavaFX Platform does
     * not provide. The supplied callback will run on the Platform thread, and
     * the method will block until the callback has completed.
     *
     * @param callback the code to run
     * @param data a data object to be passed to the callback as input
     * @return the output from the code
     * @throws java.lang.InterruptedException if the code is interrupted
     * @throws ExecutionException if a an exception occurs during execution
     */
    public static Object runAndWaitFX(JSObject callback, JSObject data) throws InterruptedException, ExecutionException {
        Object[] args = new Object[2];
        args[0] = null;
        args[1] = data;
        FutureTask<Object> future = new FutureTask(() -> {
            return callback.call("call", args);
        });
        Platform.runLater(future);
        return future.get();
    }

    /**
     * Queues a callback for execution on the Platform thread and returns
     * control to the caller.
     *
     * This method returns a {@code FutureTask<Object>} to the caller. Users can
     * poll the status of the running task by calling its {@code isDone} method.
     *
     * For details
     * <a href="http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/FutureTask.html">see
     * here</a>.
     *
     * Calling {@code get} method on the {@code FutureTask} will block execution
     * until {@code isDone} returns true and will return the object returned by
     * the callback.
     *
     * @param callback the code to run
     * @return a {@code FutureTask<Object>}
     * @throws java.lang.InterruptedException if the code is interrupted
     * @throws ExecutionException if a an exception occurs during execution
     */
    public static FutureTask<Object> runAndGoFX(JSObject callback) throws InterruptedException, ExecutionException {
        Object[] args = new Object[1];
        args[0] = null;
        final FutureTask<Object> future = new FutureTask(() -> {
            return callback.call("call", args);
        });
        Platform.runLater(() -> {
            args[1] = future;
            future.run();
        });
        return future;
    }

    /**
     * Queues a callback for execution on the Platform thread and returns
     * control to the caller.
     *
     * This method returns a {@code FutureTask<Object>} to the caller. Users can
     * poll the status of the running task by calling its {@code isDone} method.
     *
     * For details
     * <a href="http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/FutureTask.html">see
     * here</a>.
     *
     * Calling {@code get} method on the {@code FutureTask} will block execution
     * until {@code isDone} returns true and will return the object returned by
     * the callback.
     *
     * @param callback the code to run
     * @param data a data object to be passed to the callback as input
     * @return a {@code FutureTask<Object>}
     * @throws java.lang.InterruptedException if the code is interrupted
     * @throws ExecutionException if a an exception occurs during execution
     */
    public static FutureTask<Object> runAndGoFX(JSObject callback, JSObject data) throws InterruptedException, ExecutionException {
        Object[] args = new Object[3];
        args[0] = null;
        args[1] = data;
        final FutureTask<Object> future = new FutureTask(() -> {
            return callback.call("call", args);
        });
        Platform.runLater(() -> {
            args[2] = future;
            future.run();
        });
        return future;
    }

    public Node lookup(String s) {
        return getRoot().lookup(s);
    }

    public Set<Node> lookupAll(String s) {
        return getRoot().lookupAll(s);
    }

    /**
     * Returns an ArrayList of {@code FXWebWindow} instances owned by this
     * {@code FXWebApp} instance.
     *
     * @return the list of ownedWindows
     */
    public ArrayList<FXWebWindow> getOwnedWindows() {
        return ownedWindows;
    }

    void setRoot(Pane root) {
        this.root = root;
    }

    void setFxml(String fxml) {
        this.fxml = fxml;
    }

    void setCallbacks(String callbacks) {
        this.callbacks = callbacks;
    }

    /**
     * Creates a new JavaFX window external to the host browser page.
     *
     * Returns an {@code FXWebWindow} instance "owned" by the creating
     * {@code FXWebApp} instance.
     *
     * {@code FXWebWindow} subclasses the {@code FXWebApp} but instances of
     * {@code FXWebWindow} are not embedded in the host document.
     *
     * @param fxml the FXML describing the required JavaFX scene.
     * @param data a string containing data (typically as Json) that will be
     * passed to the callbacks.
     * @param callbacks the name of the JavaScript object in the host context
     * containing the callbacks (if any).
     *
     * @return a reference to the created {@code FXWebWindow} instance.
     */
    public FXWebWindow createWindow(String fxml, String data, String callbacks) {
        FXWebWindow window = new FXWebWindow(this, fxml, data, callbacks);
        return window;
    }

    /**
     * {@code FXWebWindow} subclass of {@code FXWebApp} that provides support
     * for JavaFX scenes outside of the hosting Web document.
     *
     */
    public class FXWebWindow extends FXWebApp {

        private final FXWebApp owner;

        /**
         * Constructs an {@code FXWebWindow} instances owned by the specified
         * {@code FXWebApp} instance.
         *
         * @param owner
         * @param fxml
         * @param data
         * @param callbacks
         */
        FXWebWindow(FXWebApp owner, String fxml, String data, String callbacks) {
            this.owner = owner;
            setFxml(fxml);
            setData(data);
            setCallbacks(callbacks);
            owner.getOwnedWindows().add(this);
            Platform.runLater(() -> {
                this.start(new Stage());
            });
        }

        /**
         *
         * @param stage
         */
        @Override
        public final void start(Stage stage) {

            Scene scene;

            // Set the width and height
            double w;
            double h;
            try {
                w = Double.parseDouble(getParameters().getNamed().get("w"));
            } catch (Exception ex) {
                w = 400;
            }
            try {
                h = Double.parseDouble(getParameters().getNamed().get("h"));
            } catch (Exception ex) {
                h = 300;
            }

            // Start up display
            ProgressIndicator progress = new ProgressIndicator(-1d);
            progress.setMaxSize(w / 5d, h / 5d);
            Pane tempRoot = new BorderPane(progress);
            tempRoot.setPrefSize(w, h);
            scene = new Scene(tempRoot);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.show();

            String s = this.getFxml();
            if (s != null && !s.isEmpty()) {
                // Add the default header if one has not been provided
                if (!s.startsWith("<?xml")) {
                    s = defaultFxmlHeader.concat(s);
                }
                this.setFxml(s);
                FXMLLoader loader = new FXMLLoader();

                controller = new FXWebAppController();
                loader.setController(controller);

                InputStream stream = new ByteArrayInputStream(fxml.getBytes());
                Pane root;
                try {
                    root = loader.load(stream);
                    scene.setRoot(root);
                    root.requestLayout();
                    this.setRoot(root);
                } catch (IOException ex) {
                    tempRoot.getChildren().add(new Text("FXWebApp.java encountered and error: " + ex.getMessage()));
                }
            }
        }
    }

    /**
     * Convenience, thread-safe method to invoke show on the {@code Stage} for
     * this instance.
     */
    public void show() {
        Platform.runLater(() -> {
            this.getStage().show();
        });
    }

    /**
     * Convenience, thread-safe method to invoke close on the {@code Stage} for
     * this instance.
     */
    public void close() {
        Platform.runLater(() -> {
            this.getStage().close();
        });
    }

}
