import java.util.*;
import java.io.*;

public class knows {

	static ArrayList<String> data = new ArrayList<String>();
	static ArrayList<Double> datanum = new ArrayList<Double>();
	static Scanner input = new Scanner(System.in);
	static int opt;

	public static void logo() {
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
			System.out.println("\n\n       KNOWS\n\n");
		} catch (Exception e) {
			/* Handle exceptions */ }
	}

	static void message(String msg) {
		System.out.println("\n" + msg);
		System.out.print("\nPress a key to continue...");
		input.nextLine();
	}

	static int inputclear(int... a) {
		boolean result = false;
		opt = -1;
		while (result == false) {
			System.out.print("Enter: ");
			try {
				opt = input.nextInt();
			} catch (InputMismatchException e) {
			}
			input.nextLine();
			for (int i : a) {
				if (opt != i) {
					continue;
				}
				if (opt == i) {
					result = true;
					break;
				}
			}
			if (result == false) {
				System.out.println("Enter a valid option.\n");
				continue;
			}
			continue;
		}
		return opt;
	}

	public static void main(String[] args) {
		logo();
		System.out.println("Welcome! This is KNOWS. All Rights Reserved.\n");
		System.out.print("\n1: Admin\n2: Faculty Interface.\n3: Student Interface.\n0: Exit\n\n");
		opt = (inputclear(0, 1, 2, 3));
		if (opt == 0) {
			System.out.println("\n\n\nKNOWS. Copyright 2023.");
			System.exit(0);
		}
		if (opt == 1) {
			admin();
		} else if (opt == 2) {
			faculty();
		} else {
			student();
		}
		input.close();
	}

	static void student() {
		logo();
		System.out.print("This is Student database window.\n\n\n1: Login\n0: Back.\n\n");
		opt = (inputclear(0, 1));
		if (opt == 0) {
			main(null);
		}
		if (opt == 1) {
			logo();
			System.out.print("Student login.\n\n\nStudent ID: ");
			String stuID = input.nextLine();
			System.out.print("Password: ");
			String pass = input.nextLine();
			data = readfile("database/studentcredentials.txt", 1);
			int found = -1;
			int i;
			for (i = 0; i < data.size(); i = i + 3) {
				if (data.get(i).equals(stuID)) {
					found = i;
				}
			}
			if (found == -1) {
				message("\nUser not found. Contact Admin.");
				student();
			} else {
				if (data.get(found + 2).equals(pass)) {
					message("\nLogin successful.");
					studentportal(found);
				} else {
					message("\nStudent ID and password do not match");
					student();
				}
			}
		}
	}

	static void studentportal(int found) {
		data = readfile("database/studentcredentials.txt", 1);
		String stuID = data.get(found);
		String stuName = data.get(found + 1);
		String stuPass = data.get(found + 2);
		logo();
		System.out.print("Welcome " + data.get(found + 1) + "!\n\n");
		System.out.print(
				"\n1: View personal info.\n2: Current marks summary.\n3: View grading criteria.\n4: Complete report card.\n0: Logout.\n\n");
		opt = inputclear(0, 1, 2, 3, 4);
		ArrayList<String> colist = new ArrayList<String>();
		ArrayList<String> data2 = new ArrayList<String>();
		data = readfile("database/facultyinfo.txt", 1);
		int counter;
		for (counter = 0; counter < data.size(); counter = counter + 4) {
			String filename = "database/faculty/" + data.get(counter + 1) + data.get(counter) + ".txt";
			data2 = readfile(filename, 1);
			if (data2.contains(stuID) || data2.contains(stuID + " (retake)")) {
				colist.add(data.get(counter));
				colist.add(data.get(counter + 1));
				colist.add(data.get(counter + 2));
				colist.add(data.get(counter + 3));
			}
		}
		if (opt == 0) {
			student();
		}
		if (opt == 4) {
			report(found, stuID, stuName, colist);
		} else if (opt == 1) {
			logo();
			System.out.print("Personal information.\n\n");
			System.out.print("Student ID: " + stuID);
			System.out.print("\nStudent Name: " + stuName);
			System.out.print("\n\nYour Registered Courses: \n");
			counter = 0;
			for (counter = 0; counter < colist.size(); counter = counter + 4) {
				System.out.println((counter + 4) / 4 + ": " + colist.get(counter + 1) + " by " + colist.get(counter));
			}
			System.out.print("\n\nPress 1 to view passsword. Press 0 to go Back.\n");
			opt = inputclear(0, 1);
			if (opt == 0) {
				studentportal(found);
			} else {
				System.out.println("\nYour password: " + stuPass);
				System.out.print("\nPress 0 to go Back.\n");
				if (inputclear(0) == 0) {
					studentportal(found);
				}
			}
		} else if (opt == 3) {
			logo();
			System.out.print("Grading Criteria.\n\n");
			System.out.print(
					"Percentage obtained   Grade   Grade Point\n\n   85 and above         A        4.00\n     80 - 84            A-       3.66\n     75 - 79            B+       3.33\n     71 - 74            B        3.00\n     68 - 70            B-       2.66\n     64 - 67            C+       2.33\n     61 - 63            C        2.00\n     58 - 60            C-       1.66\n     54 - 57            D+       1.30\n     50 - 53            D        1.00\n    Below  50           F        0.00\n ");
			System.out.print("\n\n0: Back.\n\n");
			if (inputclear(0) == 0) {
				studentportal(found);
			}
		} else {
			marksummary(found, colist);
		}
	}

	static void report(int found, String stuID, String stuName, ArrayList<String> colist) {
		double csum = 0;
		int cdiv = 0;
		double num = 0;
		logo();
		System.out.print("Report card.\n\n");
		System.out.println("Student ID:   " + stuID);
		System.out.println("Student Name: " + stuName + "\n\n");
		data = readfile("database/record.txt", 1);
		int csem = 1;
		for (String content : readfile("database/record.txt", 1)) {
			if (content.equals("id:" + stuID)) {
				csem = csem + 1;
			}
		}
		for (int j = 1; j < csem; j = j + 1) {
			int index1;
			int index2 = -1;
			if (true) {
				index1 = data.indexOf("id:" + stuID);
				data.set(index1, "null");
				for (int k = index1; k < data.size(); k++) {
					if (data.get(k).contains("id:")) {
						index2 = k;
						break;
					}
				}
				if (index2 == -1) {
					index2 = data.size();
				}
				List<String> data2 = new ArrayList<String>();
				data2 = data.subList(index1 + 1, index2);
				String[] arr = new String[data2.size()];
				arr = data2.toArray(arr);
				System.out.println("\n-------------------------\n");
				System.out.println("Semester: " + j + "\n");
				for (String content : arr) {

					System.out.println(content);
					if (content.contains("CGPA:")) {
						String[] cgpa = content.split(" ", 2);
						num = Double.parseDouble(cgpa[1]);

					}
					if (content.contains("Total credit hours:")) {
						String[] chours = content.split(": ", 2);
						csum = csum + num * Double.parseDouble(chours[1]);
						cdiv = cdiv + Integer.parseInt(chours[1]);
					}
				}
			}
		}
		System.out.println("\n-------------------------\n");
		if (true) {
			System.out.print("Semester: " + csem + "\n\n");
			for (int i = 0; i < colist.size(); i = i + 4) {
				if (readfile(("database/faculty/" + colist.get(i + 1) + colist.get(i) + ".txt"), 1)
						.contains(stuID + " (retake)")) {
					System.out.println("Retaking--> " + colist.get(i + 1) + " by " + colist.get(i) + " ("
							+ colist.get(i + 2) + "+" + colist.get(i + 3) + ") : ---");
				} else {
					System.out.println(colist.get(i + 1) + " by " + colist.get(i) + " (" + colist.get(i + 2) + "+"
							+ colist.get(i + 3) + ") : ---");
				}
			}
			System.out.println("\nCGPA: ---");
			System.out.println("\n-------------------------");
			System.out.println("\nAverage CGPA: " + Math.round(csum / cdiv * 100.0) / 100.0);
			System.out.println("\n-------------------------");
			System.out.print("\n0: Back.\n\n");
			if (inputclear(0) == 0) {
				studentportal(found);
			}
		}
	}

	static void marksummary(int found, ArrayList<String> colist) {
		String stuID = readfile("database/studentcredentials.txt", 1).get(found);
		String realID = stuID;
		String stuName = readfile("database/studentcredentials.txt", 1).get(found + 1);
		logo();
		System.out.print("Marks summary.\n\n");
		System.out.printf("Student ID:   %s\nStudent Name: %s\n\n\n", stuID, stuName);
		int counter;
		int credit = 0;
		double sum = 0;

		for (counter = 0; counter < colist.size(); counter = counter + 4) {
			String filename = "database/faculty/" + colist.get(counter + 1) + colist.get(counter) + ".txt";
			data = readfile(filename, 1);
			boolean retake = false;
			stuID = realID;
			if (data.contains(realID) == false) {
				stuID = stuID + " (retake)";
				retake = true;
			}
			if (retake == true) {
				System.out.print((counter + 4) / 4 + ": Retaking--> " + colist.get(counter + 1) + " by "
						+ colist.get(counter) + " (GPA: ");
			} else {
				System.out.print(
						(counter + 4) / 4 + ": " + colist.get(counter + 1) + " by " + colist.get(counter) + " (GPA: ");
			}
			if (summary(stuID, filename, 7) == 101) {
				System.out.println("No result yet)");
				continue;
			}
			int labc = Integer.parseInt(colist.get(counter + 3));
			int theoryc = Integer.parseInt(colist.get(counter + 2));
			double per = Math.ceil(summary(stuID, filename, 7));
			if (summary(stuID, filename, 6) != 0
					&& (summary(stuID, filename, 5) / summary(stuID, filename, 6)) * 100 < 50) {
				if (retake == false) {
					sum = sum + 0;
					credit = credit + theoryc + labc;
				}
				System.out.print(getgradepoint(1));
			} else if (data.get(data.indexOf(stuID) + 10).equals("---") == false
					&& Double.parseDouble(data.get(data.indexOf(stuID) + 10)) < 25) {
				if (retake == false) {
					sum = sum + 0;
					credit = credit + theoryc + labc;
				}
				System.out.print(getgradepoint(1));
			} else {
				System.out.print(getgradepoint(per));
				if (retake == false) {
					credit = credit + theoryc + labc;
					sum = sum + (getgradepoint(per) * (theoryc + labc));
				}
			}
			System.out.println(")");
		}

		if (credit != 0) {
			System.out.print(
					"\n\n-------------------------\n\nExpexted CGPA: " + Math.round(sum / credit * 100.0) / 100.0);
		} else {
			System.out.print("\n\n-------------------------\n\nExpexted CGPA: No result yet.");
		}
		System.out.print("\n\n-------------------------\n\n\n0: Back.\n\n");
		boolean clear = false;
		int option = 0;
		System.out.print("Enter: ");
		while (true) {
			try {
				option = input.nextInt();
				clear = true;
			} catch (InputMismatchException e) {
			}
			input.nextLine();
			if (clear == true) {
				if (option >= 0 && option <= ((counter + 4) / 4) - 1) {
					break;
				}
			}
			System.out.print("Enter a valid option.\n\nEnter: ");
		}
		if (option == 0) {
			studentportal(found);
		} else {
			coursemarksummary(option, found, colist);
		}
	}

	static void coursemarksummary(int opt, int found, ArrayList<String> colist) {
		opt = (opt - 1) * 4;
		String stuID = readfile("database/studentcredentials.txt", 1).get(found);
		String stuName = readfile("database/studentcredentials.txt", 1).get(found + 1);
		logo();
		System.out.print("Marks summary.\n\n");
		System.out.printf("Student ID:   %s\nStudent Name: %s\n", stuID, stuName);
		System.out.printf("\nCourse name:   %s\nProfessor:     %s\nTheory credit: %s\nLab credit:    %s\n\n",
				colist.get(opt + 1), colist.get(opt), colist.get(opt + 2), colist.get(opt + 3));
		String filename = "database/faculty/" + colist.get(opt + 1) + colist.get(opt) + ".txt";
		data = readfile(filename, 1);
		if (data.contains(stuID) == false) {
			stuID = stuID + " (retake)";
		}
		System.out.println("\n-------------------------");
		System.out.printf("\n   Assignments (10)\n\nAssignment 1: %2s\n", data.get(data.indexOf(stuID) + 1));
		System.out.printf("Assignment 2: %2s\n", data.get(data.indexOf(stuID) + 2));
		System.out.printf("Assignment 3: %2s\n", data.get(data.indexOf(stuID) + 3));
		System.out.printf("Assignment 4: %2s\n", data.get(data.indexOf(stuID) + 4));
		System.out.print("Assignment average: ");
		if (summary(stuID, filename, 1) == -1.0) {
			System.out.print("No Result");
		} else {
			System.out.print(summary(stuID, filename, 1));
		}
		System.out.printf("\n\n      Quizzes (15)\n\nQuiz 1: %2s\n", data.get(data.indexOf(stuID) + 5));
		System.out.printf("Quiz 2: %2s\n", data.get(data.indexOf(stuID) + 6));
		System.out.printf("Quiz 3: %2s\n", data.get(data.indexOf(stuID) + 7));
		System.out.printf("Quiz 4: %2s\n", data.get(data.indexOf(stuID) + 8));
		System.out.print("Quizes average: ");
		if (summary(stuID, filename, 2) == -1.0) {
			System.out.print("No Result");
		} else {
			System.out.print(summary(stuID, filename, 2));
		}
		System.out.printf("\n\n   Mid Term marks (25)\n\nMid term exam: %2s\n", data.get(data.indexOf(stuID) + 9));
		System.out.printf("\n\n   Terminal marks (50)\n\nTerminal exam: %2s\n", data.get(data.indexOf(stuID) + 10));
		if (summary(stuID, filename, 4) != 0.0) {
			System.out.println("\n\nTHEORY TOTAL: " + summary(stuID, filename, 3) + " out of "
					+ summary(stuID, filename, 4) + "  ("
					+ Math.round(((summary(stuID, filename, 3) / summary(stuID, filename, 4)) * 100.0) * 100.0) / 100.0
					+ "%)");
		} else {
			System.out.println("\n\nTHEORY TOTAL: No Result");
		}
		if (colist.get(opt + 3).equals("0") == false) {
			System.out.printf("\n-------------------------\n\n      Labs\n\nLab assignments: %2s\n",
					data.get(data.indexOf(stuID) + 11) + " (25)");
			System.out.printf("Lab mid term:    %2s\n", data.get(data.indexOf(stuID) + 12) + " (25)");
			System.out.printf("Lab terminal:    %2s\n", data.get(data.indexOf(stuID) + 13) + " (50)");
			if (summary(stuID, filename, 6) != 0.0) {
				System.out.println("\n\nLAB TOTAL: " + summary(stuID, filename, 5) + " out of "
						+ summary(stuID, filename, 6) + "  ("
						+ Math.round(((summary(stuID, filename, 5) / summary(stuID, filename, 6)) * 100.0) * 100.0)
								/ 100.0
						+ "%)");
			} else {
				System.out.println("\n\nLAB TOTAL: No Result");
			}
		}
		System.out.println("\n-------------------------");
		System.out.print("\nEXPECTED GPA: ");
		if (summary(stuID, filename, 7) == 101) {
			System.out.println("No result yet.");
		} else {
			double per = summary(stuID, filename, 7);
			if ((per > 53 && per < 54) || (per > 57 && per < 58) || (per > 60 && per < 61) || (per > 63 && per < 64)
					|| (per > 67 && per < 68) || (per > 70 && per < 71) || (per > 74 && per < 75)
					|| (per > 79 && per < 80)) {
				per = Math.ceil(summary(stuID, filename, 7));
			} else {
				per = Math.round(summary(stuID, filename, 7));
			}
			if (summary(stuID, filename, 6) != 0
					&& (summary(stuID, filename, 5) / summary(stuID, filename, 6)) * 100 < 50) {

				System.out.print(getgradepoint(1));
			} else if (data.get(data.indexOf(stuID) + 10).equals("---") == false
					&& Double.parseDouble(data.get(data.indexOf(stuID) + 10)) < 25) {
				System.out.print(getgradepoint(1));
			} else {
				System.out.print(getgradepoint(per));
			}
			System.out.println(" (" + Math.round(summary(stuID, filename, 7) * 100.0) / 100.0 + "% ~" + per + ")");
		}
		System.out.println("\n-------------------------");

		System.out.print("\n\n0: Back.\n\n");
		if (inputclear(0) == 0) {
			marksummary(found, colist);
		}
	}

	static double summary(String stuID, String filename, int option) {
		data = readfile(filename, 1);
		if (option == 1) {
			double sum = 0;
			int divide = 0;
			for (int i = 1; i < 5; i++) {
				String content = data.get(data.indexOf(stuID) + i);
				if (content.equals("---") == false) {
					sum = sum + Double.parseDouble(content);
					divide = divide + 1;
				}
			}
			if (divide == 0) {
				return -1;
			} else {
				double avg = sum / divide;
				return avg;
			}
		} else if (option == 2) {
			double sum = 0;
			int divide = 0;
			for (int i = 5; i < 9; i++) {
				String content = data.get(data.indexOf(stuID) + i);
				if (content.equals("---") == false) {
					sum = sum + Double.parseDouble(content);
					divide = divide + 1;
				}
			}
			if (divide == 0) {
				return -1;
			} else {
				double avg = sum / divide;
				return avg;
			}
		} else if (option == 3) {
			double add = 0;
			if (summary(stuID, filename, 1) == -1) {
				add = add + 0;
			} else {
				add = add + summary(stuID, filename, 1);
			}
			if (summary(stuID, filename, 2) == -1) {
				add = add + 0;
			} else {
				add = add + summary(stuID, filename, 2);
			}
			if (data.get(data.indexOf(stuID) + 9).equals("---") == false) {
				add = add + Double.parseDouble(data.get(data.indexOf(stuID) + 9));
			} else {
				add = add + 0;
			}
			if (data.get(data.indexOf(stuID) + 10).equals("---") == true) {
				add = add + 0;
			} else {
				add = add + Double.parseDouble(data.get(data.indexOf(stuID) + 10));
			}
			return add;
		} else if (option == 4) {
			int total = 0;
			if (summary(stuID, filename, 1) != -1) {
				total = total + 10;
			}
			if (summary(stuID, filename, 2) != -1) {
				total = total + 15;
			}
			if (data.get(data.indexOf(stuID) + 9).equals("---") == false) {
				total = total + 25;
			}
			if (data.get(data.indexOf(stuID) + 10).equals("---") == false) {
				total = total + 50;
			}
			return total;
		} else if (option == 5) {
			double add = 0;
			if (data.get(data.indexOf(stuID) + 11).equals("---") == false) {
				add = add + Double.parseDouble(data.get(data.indexOf(stuID) + 11));
			} else {
				add = add + 0;
			}
			if (data.get(data.indexOf(stuID) + 12).equals("---") == true) {
				add = add + 0;
			} else {
				add = add + Double.parseDouble(data.get(data.indexOf(stuID) + 12));
			}
			if (data.get(data.indexOf(stuID) + 13).equals("---") == true) {
				add = add + 0;
			} else {
				add = add + Double.parseDouble(data.get(data.indexOf(stuID) + 13));
			}
			return add;
		} else if (option == 6) {
			int total = 0;
			if (data.get(data.indexOf(stuID) + 11).equals("---") == false) {
				total = total + 25;
			}
			if (data.get(data.indexOf(stuID) + 12).equals("---") == false) {
				total = total + 25;
			}
			if (data.get(data.indexOf(stuID) + 13).equals("---") == false) {
				total = total + 50;
			}
			return total;
		} else if (option == 7) {
			if (summary(stuID, filename, 4) == 0 && summary(stuID, filename, 6) == 0) {
				return 101;
			} else if (summary(stuID, filename, 4) == 0) {
				return summary(stuID, filename, 5) / summary(stuID, filename, 6) * 100;
			} else if (summary(stuID, filename, 6) == 0) {
				return summary(stuID, filename, 3) / summary(stuID, filename, 4) * 100;
			} else {
				ArrayList<String> data2 = new ArrayList<String>();
				data2 = readfile("database/facultyinfo.txt", 1);
				String uname = data.get(0);
				int theoryc = Integer.parseInt(data2.get(data2.indexOf(uname) + 2));
				int labc = Integer.parseInt(data2.get(data2.indexOf(uname) + 3));
				return Math.round(((summary(stuID, filename, 3) / summary(stuID, filename, 4) * (theoryc))
						+ (summary(stuID, filename, 5) / summary(stuID, filename, 6) * (labc))) * 100
						/ ((theoryc) + (labc)) * 100.0) / 100.0;
			}
		} else {
			return 0;
		}
	}

	static void admin() {
		logo();
		System.out.println("This is Admin database window.");
		System.out.print("\n\n1: Login\n0: Back \n\n");
		opt = inputclear(0, 1);
		if (opt == 0) {
			main(null);
		} else {
			logo();
			System.out.println("Admin Login.\n\n");
			System.out.print("Enter username and password.\n\nUsername: ");
			String adminu = input.nextLine();
			System.out.print("Password: ");
			String adminp = input.nextLine();
			if (adminu.equals("admin") && adminp.equals("admin")) {
				message("\nAdmin Access Granted.");
				adminportal();
			} else {
				message("\nAdmin Access Denied.");
				admin();
			}
		}
	}

	static void adminportal() {
		logo();
		System.out.println("Admin Portal.\n\n");
		System.out.print(
				"1: Student Registration.\n2: Course Registration.\n3: End Semester (Disclaimer: This action cannot be undone)\n0: Back.\n\n");
		opt = inputclear(0, 1, 2, 3);
		String stuID;
		if (opt == 0) {
			admin();
		} else if (opt == 3) {
			data = readfile("database/facultyinfo.txt", 1);
			for (int i = 0; i < data.size(); i = i + 4) {
				ArrayList<String> data2 = readfile("database/faculty/" + data.get(i + 1) + data.get(i) + ".txt", 1);
				if (data.get(i + 3).equals("0") == false && data2.contains("---")) {
					message("\nCannot end the semester early");
					adminportal();
				} else if (data.get(i + 3).equals("0") == true) {
					for (int j = 1; j < data2.size(); j = j + 1) {
						if (j % 14 == 0 || j % 14 == 13 || j % 14 == 12) {
							continue;
						} else if (data2.get(j).equals("---")) {
							message("Cannot end the semester early.");
							adminportal();
						}
					}
				}
			}

			data = readfile("database/studentcredentials.txt", 1);
			int sizes = data.size();
			ArrayList<String> data2 = new ArrayList<String>();
			data2 = readfile("database/facultyinfo.txt", 1);
			for (int i = 0; i < sizes; i = i + 3) {
				ArrayList<String> data3 = new ArrayList<String>();
				data3 = readfile("database/record.txt", 1);
				data = readfile("database/studentcredentials.txt", 1);
				stuID = data.get(i);
				double sum = 0;
				double divide = 0;
				double gp = 0;
				double per = 0;
				for (int j = 0; j < data2.size(); j = j + 4) {
					String filename = "database/faculty/" + data2.get(j + 1) + data2.get(j) + ".txt";
					String coursename = data2.get(j + 1);
					if (readfile(filename, 1).contains(stuID)) {
						per = summary(stuID, filename, 7);
						if ((per > 53 && per < 54) || (per > 57 && per < 58) || (per > 60 && per < 61)
								|| (per > 63 && per < 64) || (per > 67 && per < 68) || (per > 70 && per < 71)
								|| (per > 74 && per < 75) || (per > 79 && per < 80)) {
							per = Math.ceil(summary(stuID, filename, 7));
						} else {
							per = Math.round(summary(stuID, filename, 7));
						}

						if (summary(stuID, filename, 6) != 0
								&& (summary(stuID, filename, 5) / summary(stuID, filename, 6)) * 100 < 50) {
							gp = (getgradepoint(1));
						} else if (Double.parseDouble(data.get(data.indexOf(stuID) + 10)) < 25) {
							gp = (getgradepoint(1));
						} else {
							gp = (getgradepoint(per));
						}
						String credits = data2.get(j + 2) + "+" + data2.get(j + 3);
						String writing = data2.get(j + 1) + " by " + data2.get(j) + " (" + credits + ")" + ": " + " "
								+ gp + " (" + (int) per + ")";
						double credit = Double.parseDouble(data2.get(j + 2)) + Double.parseDouble(data2.get(j + 3));
						sum = sum + (gp * credit);
						divide = divide + credit;
						if (divide <= 4) {
							writefile("database/record.txt", "id:" + stuID);
						}
						writefile("database/record.txt", writing);
					} else if (readfile(filename, 1).contains(stuID + " (retake)")) {

						per = summary((stuID + " (retake)"), filename, 7);
						if ((per > 53 && per < 54) || (per > 57 && per < 58) || (per > 60 && per < 61)
								|| (per > 63 && per < 64) || (per > 67 && per < 68) || (per > 70 && per < 71)
								|| (per > 74 && per < 75) || (per > 79 && per < 80)) {
							per = Math.ceil(per);
						} else {
							per = Math.round(per);
						}
						if (summary((stuID + " (retake)"), filename, 6) != 0
								&& (summary((stuID + " (retake)"), filename, 5)
										/ summary((stuID + " (retake)"), filename, 6)) * 100 < 50) {
							gp = (getgradepoint(1));
						} else if (Double.parseDouble(data.get(data.indexOf(stuID + " (retake)") + 10)) < 25) {
							gp = (getgradepoint(1));
						} else {
							gp = (getgradepoint(per));

						}
						String credits = data2.get(j + 2) + "+" + data2.get(j + 3);
						String writing = data2.get(j + 1) + " by " + data2.get(j) + " (" + credits + ")" + ": " + " "
								+ gp + " (" + (int) per + ")";
						ArrayList<String> datanew = readfile("database/record.txt", 1);
						int index1 = -1;
						int index2 = -1;
						boolean foundindex1 = false;
						boolean foundcourse = false;
						for (int k = 0; k < datanew.size(); k++) {
							if (datanew.get(k).equals("id:" + stuID) && foundcourse == false) {
								foundindex1 = true;
								index1 = k;
							} else if (datanew.get(k).contains("id:") && foundcourse == true) {
								index2 = k;
								break;
							}
							if (datanew.get(k).contains(coursename) && foundindex1 == true) {
								foundcourse = true;
							}
						}
						if (index2 == -1) {
							index2 = datanew.size();
						}
						List<String> datanew2 = new ArrayList<String>();
						datanew2 = datanew.subList(index1 + 1, index2);
						int count = 0;
						double sumcgpa = 0;
						int div = 0;
						int credit = 0;
						for (String contents : datanew2) {
							if (contents.contains(coursename)) {
								datanew.set(datanew2.indexOf(contents) + index1 + 1, writing);
								contents = writing;

							}
							if (count <= datanew2.size() - 5) {
								String[] chour = contents.split("\\+", 2);
								char[] chour1 = chour[0].toCharArray();
								char[] chour2 = chour[1].toCharArray();
								credit = Character.getNumericValue(chour2[0])
										+ Character.getNumericValue(chour1[chour1.length - 1]);
								String[] content = contents.split("  ", 2);
								String[] cgpa = content[1].split(" ", 2);
								sumcgpa = sumcgpa + (Double.parseDouble(cgpa[0]) * credit);
								count = count + 1;
								div = div + credit;
							}
						}
						double ncgpa = Math.round(sumcgpa * 100.0 / div) / 100.0;
						datanew.set(index2 - 3, "CGPA: " + ncgpa);
						try {
							File myFile = new File("database/record.txt");
							myFile.delete();
							myFile.createNewFile();
							for (String writeinfile : datanew) {
								writefile("database/record.txt", writeinfile);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (divide != 0) {
					writefile("database/record.txt", "\nCGPA: " + Math.round(sum / divide * 100.0) / 100.0);
					writefile("database/record.txt", "\nTotal credit hours: " + (int) divide);
				}
			}
			data = readfile("database/facultyinfo.txt", 1);
			for (int k = 0; k < data.size(); k = k + 4) {
				String filename = "database/faculty/" + data.get(k + 1) + data.get(k) + ".txt";
				try {
					File myFile = new File(filename);
					myFile.delete();
					myFile.createNewFile();
					writefile(filename, data.get(k));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			message("The current semester has now ended.");
			main(null);
		} else if (opt == 1) {
			logo();
			System.out.println(
					"\n\nTo allow students to access their portal, enter each student name, their unique ID no. and password.\n\n");
			System.out.print("Student ID: ");
			stuID = input.nextLine();
			data = readfile("database/studentcredentials.txt", 3);
			if (data.contains(stuID)) {
				message("\nStudentID already exists.");
				adminportal();
			}
			System.out.print("Student name:  ");
			String stuName = input.nextLine();
			System.out.print("Password: ");
			String pass = input.nextLine();
			writefile("database/studentcredentials.txt", stuID);
			writefile("database/studentcredentials.txt", stuName);
			writefile("database/studentcredentials.txt", pass);
			message("\n" + stuName + " Registration Completed.");
			adminportal();
		} else {
			coursereg();
		}
	}

	static void coursereg() {
		while (true) {
			data = readfile("database/studentcredentials.txt", 3);
			logo();
			System.out.println("Registering Courses: \n");
			int count = 0;
			for (String ids : data) {
				System.out.println((count + 1) + ": " + ids);
				count = count + 1;
			}
			System.out.print("\n0: Back.");
			boolean clear = false;
			int option = 0;
			while (true) {
				try {
					System.out.print("\n\nEnter: ");
					option = input.nextInt();
					clear = true;
				} catch (InputMismatchException e) {
				}
				input.nextLine();
				if (clear == true) {
					if (option >= 0 && option <= count) {
						break;
					}
				}
				System.out.print("Enter a valid option.");
			}
			if (option == 0) {
				adminportal();
			}
			String stuID = data.get(option - 1);
			int sum = 0;
			for (String content : readfile("database/record.txt", 1)) {
				if (content.equals("id:" + stuID)) {
					sum = sum + 1;
				}
			}
			if (sum >= 12) {
				message("\n" + stuID + " have completed maximum semesters (ie. 12)");
				coursereg();

			} else {
				coursereg2(stuID);
			}
		}
	}

	static void coursereg2(String stuID) {
		while (true) {
			logo();
			System.out.println("Registering Courses: \n");
			System.out.println("Student ID: " + stuID + "\n\n");
			data = readfile("database/facultyinfo.txt", 1);
			int i = 0;
			for (String words : data) {
				if ((i - 1) % 4 == 0) {
					System.out.println((i + 4) / 4 + ": " + data.get(i) + " by " + data.get(i - 1));
				}
				i = i + 1;
			}
			System.out.println("0: Complete Registration.");
			boolean clear = false;
			int opt = 0;
			while (true) {
				try {
					System.out.print("\n\nEnter: ");
					opt = input.nextInt();
					clear = true;
				} catch (InputMismatchException e) {
				}
				input.nextLine();
				if (clear == true) {
					if (opt >= 0 && opt <= i / 4) {
						break;
					}
				}
				System.out.print("Enter a valid option.");
			}
			if (opt == 0) {
				coursereg();
			}
			String sub = data.get((opt * 4) - 3);
			String filename = "database/faculty/" + data.get((opt * 4) - 3) + data.get((opt * 4) - 4) + ".txt";
			data = readfile(filename, 1);
			i = 0;
			data = readfile("database/record.txt", 1);
			boolean retake = false;
			int csem = 1;
			for (String content : readfile("database/record.txt", 1)) {
				if (content.equals("id:" + stuID)) {
					csem = csem + 1;
				}
			}
			for (int j = 1; j < csem; j = j + 1) {
				int index1;
				int index2 = -1;
				if (true) {
					index1 = data.indexOf("id:" + stuID);
					data.set(index1, "null");
					for (int k = index1; k < data.size(); k++) {
						if (data.get(k).contains("id:")) {
							index2 = k;
							break;
						}
					}
					if (index2 == -1) {
						index2 = data.size();
					}
					List<String> data2 = new ArrayList<String>();
					data2 = data.subList(index1 + 1, index2);
					for (String contents : data2) {
						if (contents.contains(sub)) {
							retake = true;
						}
					}
				}
			}
			data = readfile(filename, 1);
			if (retake == true) {
				System.out.println("\nYou are retaking the course: " + sub);
			}
			if (data.contains(stuID + " (retake)") == false && data.contains(stuID) == false) {
				if (retake == true) {
					writefile(filename, stuID + " (retake)");
				} else {
					writefile(filename, stuID);
				}
				while (i < 13) {
					writefile(filename, "---");
					i = i + 1;
				}
			} else {
				message("\nAlready registered");
				continue;
			}
			message("\nCourse: " + sub + " registered.");
			continue;
		}
	}

	static void faculty() {
		logo();
		System.out.println("This is Faculty database window.");
		System.out.print("\n\n1: Signup for new users. \n2: Login for already existing accounts.\n0: Back\n\n");
		opt = inputclear(0, 1, 2);
		if (opt == 0) {
			System.out.println("\n\n");
			main(null);
		}
		if (opt == 1) {
			facultysignup();
		} else {
			facultylogin();
		}
	}

	static void facultysignup() {
		logo();
		System.out.println("Signup with a unique username and a password.\n");
		System.out.print("\nEnter new username: ");
		String nuname = input.nextLine();
		data = readfile("database/facultycredentials.txt", 2);
		if (data.contains(nuname)) {
			message("Username already exists.");
			faculty();
		}
		writefile("database/facultycredentials.txt", nuname);
		System.out.print("Enter new password: ");
		writefile("database/facultycredentials.txt", input.nextLine());
		System.out
				.println("\n\nGathering personal information. This will only take few minutes.\n\nUsername: " + nuname);
		writefile("database/facultyinfo.txt", nuname);
		System.out.print("Course: ");
		String coursename = input.nextLine();
		writefile("database/facultyinfo.txt", coursename);
		System.out.print("Theory credit hours: ");
		writefile("database/facultyinfo.txt", input.nextLine());
		System.out.print("Lab credit hours: ");
		writefile("database/facultyinfo.txt", input.nextLine());
		String filename = "database/faculty/" + coursename + nuname + ".txt";
		writefile(filename, nuname);
		message("Registration completed.");
		faculty();
	}

	static void facultylogin() {
		logo();
		System.out.println("Login  with your username and password.\n");
		int found = -1;
		System.out.print("\nEnter username: ");
		String uname = input.nextLine();
		System.out.print("Enter password: ");
		String pass = input.nextLine();
		data = readfile("database/facultycredentials.txt", 1);
		for (int i = 0; i < data.size(); i = i + 2) {
			if (data.get(i).equals(uname)) {
				found = i;
			}
		}
		if (found == -1) {
			message("\nAccount not found.");
			faculty();
		}
		if (pass.equals(data.get(found + 1))) {
			message("\nAccess Granted.");
			facultyportal(uname);
		} else {
			message("\nUsername and Password does not match.");
			faculty();
		}
	}

	static void facultyportal(String uname) {
		logo();
		System.out.println("database/faculty Portal.\n\n");
		System.out.println("Welcome " + uname + "!\n\n");
		System.out.print("1: View personal information.\n2: Add student marks.\n0: Logout.\n\n");
		opt = inputclear(0, 1, 2);
		if (opt == 0) {
			faculty();
		}
		data = readfile("database/facultyinfo.txt", 1);
		if (opt == 1) {
			logo();
			System.out.println("Personal Information.\n");
			System.out.println("\nUsername: " + uname);
			System.out.println("Course: " + data.get(data.indexOf(uname) + 1));
			System.out.println("Theory credit hours: " + data.get(data.indexOf(uname) + 2));
			System.out.println("Lab credit hours: " + data.get(data.indexOf(uname) + 3));
			System.out.print("\nPress 1 to see password. Press 0 to go back: ");
			opt = inputclear(0, 1);
			if (opt == 0) {
				facultyportal(uname);
			} else {
				data = readfile("database/facultycredentials.txt", 1);
				System.out.println("Password: " + data.get(data.indexOf(uname) + 1));
				System.out.print("\nEnter 0 to go back: ");
				if (inputclear(0) == 0) {
					facultyportal(uname);
				}
			}
		}
		if (opt == 2) {
			preaddmarks(uname, data.get(data.indexOf(uname) + 1), data.get(data.indexOf(uname) + 2),
					data.get(data.indexOf(uname) + 3));
		}
	}

	static void preaddmarks(String uname, String course, String theoryc, String labc) {
		logo();
		System.out.printf("Adding student marks.\n\n\nProfessor:     %s\nCourse Name:   %s\nCredit hours:  %s + %s",
				uname, course, theoryc, labc);
		String filename = "database/faculty/" + course + uname + ".txt";
		data = readfile(filename, 1);
		System.out.println("\n\n");
		int counter = 0;
		for (String ids : data) {
			if ((counter) % 14 == 1) {
				System.out.println((int) (counter + 13) / 14 + ": " + data.get(counter));
			}
			counter = counter + 1;
		}
		System.out.print("0: Back\n\nEnter: ");
		boolean clear = false;
		int option = 0;
		while (true) {
			try {
				option = input.nextInt();
				clear = true;
			} catch (InputMismatchException e) {
			}
			input.nextLine();
			if (clear == true) {
				if (option >= 0 && option <= ((counter + 13) / 14) - 1) {
					break;
				}
			}
			System.out.print("Enter a valid option.\n\nEnter: ");
		}
		if (option == 0) {
			facultyportal(uname);
		}
		String stuID = data.get((option * 14) - 13);
		data = readfile("database/studentcredentials.txt", 1);
		String stuName = data.get(data.indexOf(stuID) + 1);
		if (stuID.contains("(retake)")) {
			String[] stuIDs = stuID.split(" ", 2);
			stuID = stuIDs[0];
			data = readfile("database/studentcredentials.txt", 1);
			stuName = data.get(data.indexOf(stuID) + 1);
		}
		preaddmarks2(uname, course, stuID, stuName, theoryc, labc);
	}

	static void preaddmarks2(String uname, String course, String stuID, String stuName, String theoryc, String labc) {
		logo();

		System.out.printf(
				"Adding student marks.\n\n\nProfessor:     %s\nCourse Name:   %s\nCredit hours:  %s + %s\n\nStuID:   %s\nName:    %s",
				uname, course, theoryc, labc, stuID, stuName);
		System.out.println("\n\n\n1: Assignments. \n2: Quizes. \n3: MidTerm Exams. \n4: Terminal Exams.");
		if (labc.equals("0") == false) {
			System.out.println("5: Labs.");
		}
		System.out.println("0: Back.\n");
		if (labc.equals("0") == false) {
			opt = inputclear(0, 1, 2, 3, 4, 5);
		} else {
			opt = inputclear(0, 1, 2, 3, 4);
		}
		if (opt == 0) {
			preaddmarks(uname, course, theoryc, labc);
		} else {
			addmarks(opt, uname, course, stuID, stuName, theoryc, labc);
		}
	}

	static void addmarks(int option, String uname, String course, String stuID, String stuName, String theoryc,
			String labc) {
		logo();

		System.out.printf(
				"Adding student marks.\n\n\nProfessor:     %s\nCourse Name:   %s\nCredit hours:  %s + %s\n\nStuID:   %s\nName:    %s",
				uname, course, theoryc, labc, stuID, stuName);
		String filename = "database/faculty/" + course + uname + ".txt";
		data = readfile(filename, 1);
		int start = data.indexOf(stuID);
		if (start == -1) {
			start = data.indexOf(stuID + " (retake)");
		}
		String marknew;
		if (option == 1) {
			String ass1 = data.get(start + 1);
			String ass2 = data.get(start + 2);
			String ass3 = data.get(start + 3);
			String ass4 = data.get(start + 4);
			System.out.print("\n\n\nEnter assignments marks out of 10.\n\n1: Update Assignment 1: " + ass1
					+ "\n2: Update Assignment 2: " + ass2 + "\n3: Update Assignment 3: " + ass3
					+ "\n4: Update Assignment 4: " + ass4 + "\n0: Back.\n\n");
			opt = inputclear(0, 1, 2, 3, 4);
			if (opt == 0) {
				preaddmarks2(uname, course, stuID, stuName, theoryc, labc);
			}
			if (opt == 1) {
				System.out.print("\nEnter Assignment 1 marks: ");
				marknew = input.nextLine();
				data.set(start + 1, marknew);
			}
			if (opt == 2) {
				System.out.print("\nEnter Assignment 2 marks: ");
				marknew = input.nextLine();
				data.set(start + 2, marknew);
			}
			if (opt == 3) {
				System.out.print("\nEnter Assignment 3 marks: ");
				marknew = input.nextLine();
				data.set(start + 3, marknew);
			}
			if (opt == 4) {
				System.out.print("\nEnter Assignment 4 marks: ");
				marknew = input.nextLine();
				data.set(start + 4, marknew);
			}
		} else if (option == 2) {
			String quiz1 = data.get(start + 5);
			String quiz2 = data.get(start + 6);
			String quiz3 = data.get(start + 7);
			String quiz4 = data.get(start + 8);
			System.out.print("\n\n\nEnter the quizzes marks out of 15.\n\n1: Update Quiz 1: " + quiz1
					+ "\n2: Update Quiz 2: " + quiz2 + "\n3: Update Quiz 3: " + quiz3 + "\n4: Update Quiz 4: " + quiz4
					+ "\n0: Back.\n\n");
			opt = inputclear(0, 1, 2, 3, 4);
			if (opt == 0) {
				preaddmarks2(uname, course, stuID, stuName, theoryc, labc);
			}
			if (opt == 1) {
				System.out.print("\nEnter Quiz 1 marks: ");
				marknew = input.nextLine();
				data.set(start + 5, marknew);
			}
			if (opt == 2) {
				System.out.print("\nEnter Quiz 2 marks: ");
				marknew = input.nextLine();
				data.set(start + 6, marknew);
			}
			if (opt == 3) {
				System.out.print("\nEnter Quiz 3 marks: ");
				marknew = input.nextLine();
				data.set(start + 7, marknew);
			}
			if (opt == 4) {
				System.out.print("\nEnter Quiz 4 marks: ");
				marknew = input.nextLine();
				data.set(start + 8, marknew);
			}
		} else if (option == 3) {
			String mids = data.get(start + 9);
			System.out.print("\n\n\nEnter Mid Term Exam marks out of 25.\n\n1: Update Mid Term Exam marks: " + mids
					+ "\n0: Back\n\n");
			opt = inputclear(0, 1);
			if (opt == 0) {
				preaddmarks2(uname, course, stuID, stuName, theoryc, labc);
			}
			if (opt == 1) {
				System.out.print("\nEnter Mid Term marks: ");
				marknew = input.nextLine();
				data.set(start + 9, marknew);
			}
		} else if (option == 4) {
			String finals = data.get(start + 10);
			System.out.print("\n\n\nEnter Terminal Exam marks out of 50.\n\n1: Update Terminal Exam marks: " + finals
					+ "\n0: Back\n\n");
			opt = inputclear(0, 1);
			if (opt == 0) {
				preaddmarks2(uname, course, stuID, stuName, theoryc, labc);
			}
			if (opt == 1) {
				System.out.print("\nEnter Terminal Exam marks: ");
				marknew = input.nextLine();
				data.set(start + 10, marknew);
			}
		} else if (option == 5 && labc.equals("0") == false) {
			String labass = data.get(start + 11);
			String labmids = data.get(start + 12);
			String labfinals = data.get(start + 13);
			System.out.print(
					"\n\n\nEnter Lab Assignment (out of 25), Lab Mid Term (out of 25) and Lab Terminal (out of 50) marks.\n\n1: Update Lab Assignments marks: "
							+ labass + "\n2: Update Lab Mid Term marks:    " + labmids
							+ "\n3: Update Lab Terminal marks:    " + labfinals + "\n0: Back\n\n");
			opt = inputclear(0, 1, 2, 3);
			if (opt == 0) {
				preaddmarks2(uname, course, stuID, stuName, theoryc, labc);
			}
			if (opt == 1) {
				System.out.print("\nEnter Lab Assignment marks: ");
				marknew = input.nextLine();
				data.set(start + 11, marknew);
			}
			if (opt == 2) {
				System.out.print("\nEnter Lab Mid Term marks: ");
				marknew = input.nextLine();
				data.set(start + 12, marknew);
			}
			if (opt == 3) {
				System.out.print("\nEnter Lab Terminal marks: ");
				marknew = input.nextLine();
				data.set(start + 13, marknew);
			}
		}
		try {
			File myFile = new File(filename);
			myFile.delete();
			myFile.createNewFile();
			for (String content : data) {
				writefile(filename, content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n\nUpdated with new marks.");
		addmarks(option, uname, course, stuID, stuName, theoryc, labc);
	}

	static double getgradepoint(double per) {
		if (per >= 85) {
			return (4.00);
		}
		if (per <= 84 && per >= 80) {
			return (3.66);
		}
		if (per <= 79 && per >= 75) {
			return (3.33);
		}
		if (per <= 74 && per >= 71) {
			return (3.00);
		}
		if (per <= 70 && per >= 68) {
			return (2.66);
		}
		if (per <= 67 && per >= 64) {
			return (2.33);
		}
		if (per <= 63 && per >= 61) {
			return (2.00);
		}
		if (per <= 60 && per >= 58) {
			return (1.66);
		}
		if (per <= 57 && per >= 54) {
			return (1.30);
		}
		if (per <= 53 && per >= 50) {
			return (1.00);
		} else {
			return (0.00);
		}
	}

	static void writefile(String fname, String content) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname, true));
			writer.write(content + "\n");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void writefile(String fname, double content) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname, true));
			writer.write(content + "\n");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static ArrayList<String> readfile(String fname, int skip) {
		int counter = skip - 1;
		ArrayList<String> datasend = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fname));
			String line;
			while ((line = reader.readLine()) != null) {
				counter = counter + 1;
				if (counter % skip == 0) {
					datasend.add(line);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datasend;
	}

}