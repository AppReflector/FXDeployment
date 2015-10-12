/**
 * <p>
 * Copyright (c) 2015, Malcolm Lidierth, UK. All rights reserved.
 *
 * Modifications for use with Reflector Copyright (c) 2015, AppReflector Ltd.,
 * UK. All rights reserved.
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

import java.util.LinkedHashMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import netscape.javascript.JSObject;

/**
 * FXWebAppController class
 *
 * @author ML
 */
public class FXWebAppController implements FXWebAppControllerInterface {

    /**
     * {@inheritDoc }
     */
    @Override
    public void initialize(FXWebApp app, String callbacks) {

        JSObject context = app.getHostContext();
        JSObject member = (JSObject) context.getMember(callbacks);

        // Run through the contents of the reflector$callbacks.js defined
        // callbacks. If this control has a specific callback defined for it,
        // use that callback. otherwise use the standard callback for this control
        // type.
        // Add Java content to the listeners as required.
        LinkedHashMap<String, Object> map = app.getCallbackObjectMap();

        if (map.isEmpty()) {
            findComponents(app, map);
        }

        map.keySet().stream().map((s)
                -> app.getRoot().lookup("#" + s)).forEach((Node node) -> {
                    if (node != null) {
                        switch (node.getClass().toString().replace("class ", "")) {
                            case "javafx.scene.control.Button":
                            case "javafx.scene.control.CheckBox":
                            case "javafx.scene.control.ToggleButton":
                            case "javafx.scene.control.RadioButton":
                            case "javafx.scene.control.MenuButton":
                            case "javafx.scene.control.Hyperlink":
                                if ((Boolean) member.call("hasOwnProperty", node.getId().concat("_callback"))) {
                                    ((ButtonBase) node).setOnAction((ActionEvent ev) -> {
                                        // Call the JS callback
                                        FXCall(member, node.getId().concat("_callback"), node, ev, map.get(node.getId()), app.getData());
                                    });
                                } else {
                                    String str = "";
                                    switch (node.getClass().toString().replace("class ", "")) {
                                        case "javafx.scene.control.Button":
                                            str = "button";
                                            break;
                                        case "javafx.scene.control.CheckBox":
                                            str = "checkbox";
                                            break;
                                        case "javafx.scene.control.ToggleButton":
                                            str = "togglebutton";
                                            break;
                                        case "javafx.scene.control.RadioButton":
                                            str = "radiobutton";
                                            break;
                                        case "javafx.scene.control.MenuButton":
                                            str = "menubutton";
                                            break;
                                        case "javafx.scene.control.Hyperlink":
                                            str = "hyperlink";
                                    }
                                    final String callback = str + "$callback";
                                    ((ButtonBase) node).setOnAction((ActionEvent ev) -> {
                                        // Call the JS callback
                                        FXCall(member, callback, node, ev, map.get(node.getId()), app.getData());
                                    });
                                }
                                break;
                            case "javafx.scene.control.ComboBox":
                                if ((Boolean) member.call("hasOwnProperty", node.getId().concat("_callback"))) {
                                    ((ComboBox) node).setOnAction(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent ev) {
                                            // Call the JS callback
                                            FXCall(member, node.getId().concat("_callback"), node, ev, map.get(node.getId()), app.getData());
                                        }
                                    });
                                } else {
                                    ((ComboBox) node).setOnAction(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent ev) {
                                            // Call the JS callback
                                            FXCall(member, "combobox$callback", node, ev, map.get(node.getId()), app.getData());
                                        }
                                    });
                                }
                                break;
                            case "javafx.scene.control.ChoiceBox":
                                //TODO
                                break;

                            case "javafx.scene.control.Slider":
                                if ((Boolean) member.call("hasOwnProperty", node.getId().concat("_callback"))) {
                                    ((Slider) node).setOnMouseReleased((MouseEvent ev) -> {
                                        // Call the JS callback
                                        FXCall(member, node.getId().concat("_callback"), node, ev, map.get(node.getId()), app.getData());
                                    });
                                } else {
                                    ((Slider) node).setOnMouseReleased((MouseEvent ev) -> {
                                        // Call the JS callback
                                        FXCall(member, "slider$callback", node, ev, map.get(node.getId()), app.getData());
                                    });
                                }
                                break;
                            case "javafx.scene.control.TextField":
                                if ((Boolean) member.call("hasOwnProperty", node.getId().concat("_callback"))) {
                                    ((TextField) node).setOnAction((ActionEvent ev) -> {
                                        // Call the JS callback
                                        FXCall(member, node.getId().concat("_callback"), node, ev, map.get(node.getId()), app.getData());
                                    });
                                } else {
                                    ((TextField) node).setOnAction((ActionEvent ev) -> {
                                        // Call the JS callback
                                        FXCall(member, "textfield$callback", node, ev, map.get(node.getId()), app.getData());
                                    });
                                }
                                break;
                            case "javafx.scene.control.ListView":
                                if ((Boolean) member.call("hasOwnProperty", node.getId().concat("_callback"))) {
                                    ((ListView) node).setOnEditCommit(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent ev) {
                                            // Call the JS callback
                                            FXCall(member, node.getId().concat("_callback"), node, ev, map.get(node.getId()), app.getData());
                                        }
                                    });
                                } else {
                                    ((ListView) node).setOnEditCommit(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent ev) {
                                            // Call the JS callback
                                            FXCall(member, "listview$callback", node, ev, map.get(node.getId()), app.getData());
                                        }
                                    });
                                }
                                break;
                            default:
                        }
                    }
                });
    }

    /**
     * Calls the relevant JavaScript callback from the JavaFX Platform thread.
     *
     *
     * @param member the JSObject defined the callbacks
     * @param method the name of the callback to invoke
     * @param args the arguments to pass the callback
     */
    private void FXCall(JSObject member, String method, Object... args) {
        Platform.runLater(() -> {
            member.call(method, args);
        });

    }

    private void findComponents(final FXWebApp app, final LinkedHashMap map) {
        Parent root = app.getRoot();
        getDescendants(app, root, map);
    }

    private void getDescendants(FXWebApp app, Node node, LinkedHashMap map) {
        //app.getHostContext().eval("console.log('getDescendants: " + node.getClass().toString() + " - " + node.getId() + "');");
        if (node.getId() != null && !node.getId().isEmpty()) {
            map.put(node.getId(), null);
        }
        if (node instanceof Parent) {
            ((Parent) node).getChildrenUnmodifiable().stream().forEach((Node child) -> {
                getDescendants(app, child, map);
            });
        }
    }
}
