package de.aittr.lms.UITests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class GoToMyHomeWorkUITests extends TestBaseUI {
    @BeforeMethod
    public void precondition() {
        app.getUserUI().loginWithData("a04@dev-lms.de", "LMS-dev-pass-2024");
        app.getUserUI().closeLoginMessage();
    }

    @Test
    public void isMyHomeWorkTest() {
        report.add("goToMyHomeWorkTest" + System.lineSeparator()); // надо для report
        app.getCSVReaderUI().clickOnSelectYourGroup();
        List<WebElement> groups = app.getCSVReaderUI().getDropdownListOfGroups();
        List<String> listOfGroups = new ArrayList<>();
        listOfGroups = app.getCSVReaderUI().readerHelper()[0];

        if (listOfGroups.size() == 0 || listOfGroups.get(0).equals("")) {
            listOfGroups.clear();
            for (WebElement item : groups) {
                listOfGroups.add(item.getText());
            }
        }
        for (int j = 0; j < listOfGroups.size(); j++) {
            if (app.getCSVReaderUI().selectMyGroup(listOfGroups.get(j))) {
                app.getCSVReaderUI().clickOnSelectedGroup(listOfGroups.get(j));
                report.add("********************************************************************");
                report.add("                       Group: " + listOfGroups.get(j));
                report.add("********************************************************************");

                if (app.getCSVReaderUI().isModulePresent()) {
                    app.getCSVReaderUI().clickOnSelectModule();
                    List<WebElement> modules = app.getCSVReaderUI().getDropdownListOfModules();
                    List<String> listOfModules = new ArrayList<>();
                    listOfModules = app.getCSVReaderUI().readerHelper()[1];

                    if (listOfModules.size() == 0 || listOfModules.get(0).equals("")) {
                        listOfModules.clear();
                        for (WebElement item : modules) {
                            listOfModules.add(item.getText());
                        }
                    }
                    for (int l = 0; l < listOfModules.size(); l++) {
                        if (app.getCSVReaderUI().selectMyModule(listOfModules.get(l))) {
                            app.getCSVReaderUI().clickOnSelectedModule(listOfModules.get(l));
                            report.add("--------------------------------------------------------------------");
                            report.add("                       Module: " + listOfModules.get(l));
                            report.add("--------------------------------------------------------------------");
                            report.add("     Lesson   |                   MyHomeWork                       |");
                            report.add("--------------------------------------------------------------------");

                            if (app.getCSVReaderUI().isLessonPresent()) {
                                app.getCSVReaderUI().clickOnSelectLesson();
                                List<WebElement> lessons = app.getCSVReaderUI().getDropdownListOfLessons();
                                List<String> listOfLessons = new ArrayList<>();
                                listOfLessons = app.getCSVReaderUI().readerHelper()[2];
                                // System.out.println(listOfLessons);
                                if (listOfLessons.size() == 0 || listOfLessons.get(0).equals("")) {
                                    listOfLessons.clear();

                                    for (WebElement item : lessons) {
                                        listOfLessons.add(item.getText());
                                    }
                                }
                                for (int i = 0; i < listOfLessons.size(); i++) {
                                    if (app.getCSVReaderUI().selectMyLesson(listOfLessons.get(i))) {
                                        app.getCSVReaderUI().clickOnSelectedLesson(listOfLessons.get(i));
                                        String myHomeWork = "o";

                                        if (app.getCSVReaderUI().goToMyHomeWorkPresent()) {
                                            // Проверяем вкладку ПЛАН и наличие там текста
                                            myHomeWork = "+";
                                        } else {
                                            myHomeWork = "-";
                                        }
                                        app.getCSVReaderUI().pause(1000);
                                        report.add(String.format("    %-5s |                       %-5s%n", listOfLessons.get(i), myHomeWork + "                            |"));
                                        app.getCSVReaderUI().clickOnNextSelectedLesson();
                                        app.getUserUI().pause(2000);
                                    } else {
                                        report.add("В группе " + listOfGroups.get(j) + " в модуле " + listOfModules.get(l) + "  " + listOfLessons.get(i) + " еще нет");
                                    }
                                }
                            } else {
                                report.add("В группе " + listOfGroups.get(j) + " в модуле " + listOfModules.get(l) + " уроков еще нет");
                            }
                        } else {
                            report.add("В группе " + listOfGroups.get(j) + " модуля " + listOfModules.get(l) + " еще нет");
                        }
                        app.getCSVReaderUI().clickOnNextSelectedModule();
                        app.getUserUI().pause(1000);
                    }
                } else {
                    report.add("В группе " + listOfGroups.get(j) + "  модулей еще нет");
                }
                app.getCSVReaderUI().scrollPageUp();
                app.getCSVReaderUI().clickOnNextSelectedGroup();
                app.getUserUI().pause(1000);
            } else {
                report.add("Группы " + listOfGroups.get(j) + " еще нет");
            }
        }
    }

    @AfterMethod
    public void postCondition() {
        app.getUserUI().logOut();
    }
}


