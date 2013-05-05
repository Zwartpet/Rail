/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handler;

import model.Path;
import model.Vertex;

/**
 *
 * @author Tony
 */
public class PathTranslator {

    public static String translate(Path path) {
        String inBetween = "";

        for (int i = 0; i < path.getPath().size() - 2; i++) {
            Vertex firstVertex = path.getPath().get(i);
            Vertex nextVertex = path.getPath().get(i + 1);
            Vertex afterVertex = path.getPath().get(i + 2);

            int startDirection = getWorldDirection(firstVertex, nextVertex);
            int nextDirection = getWorldDirection(nextVertex, afterVertex);
            char localDirection = getLocalDirection(startDirection, nextDirection);
            inBetween += localDirection;
        }


        String finalTranslatePath = "";
        int tempNodes = 0;
        for (int i = 0; i < inBetween.length(); i++) {
            char testChar = inBetween.charAt(i);

            switch (testChar) {
                case 'l':
                    if (tempNodes != 0) {
                        finalTranslatePath += tempNodes;
                    }

                    finalTranslatePath += 'L';
                    tempNodes = 0;
                    break;
                case 'r':
                    if (tempNodes != 0) {
                        finalTranslatePath += tempNodes;
                    }
                    finalTranslatePath += 'R';
                    tempNodes = 0;
                    break;
                case 'n':
                    finalTranslatePath += 1;
//                    tempNodes++;
                    break;
            }
        }

//        if (tempNodes != 0) {
//            finalTranslatePath += tempNodes;
//        }
        finalTranslatePath += "1S";
        return finalTranslatePath;

    }

    private static int getWorldDirection(Vertex firstVertex, Vertex secondVertex) {
        int xDifference = (int) (secondVertex.getxPosition() - firstVertex.getxPosition());
        int yDifference = (int) (secondVertex.getyPosition() - firstVertex.getyPosition());


        if (xDifference == 1) {
            return 0;

        } else if (xDifference == -1) {
            return 2;
        } else if (yDifference == 1) {
            return 1;
        } else if (yDifference == -1) {
            return 3;
        }
        return -1;
    }

    private static char getLocalDirection(int startDirection, int nextDirection) {
        if (startDirection == 0 && nextDirection == 3 || startDirection == 3 && nextDirection == 2 || startDirection == 2 && nextDirection == 1 || startDirection == 1 && nextDirection == 0) {
            return 'l';
        } else if (startDirection == 0 && nextDirection == 1 || startDirection == 3 && nextDirection == 0 || startDirection == 2 && nextDirection == 3 || startDirection == 1 && nextDirection == 2) {
            return 'r';
        } else if (startDirection == nextDirection) {
            return 'n';
        }
        return 'f';
    }
}
