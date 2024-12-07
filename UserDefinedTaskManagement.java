import java.util.*;

class Task {
    private static int nextId = 1;
    private int taskId;
    private String title;
    private String description;
    private Date deadline;
    private int priority;
    private Set<Integer> dependencies;
    private boolean completed;

    public Task(String title, String description, Date deadline, int priority) {
        this.taskId = nextId++;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.dependencies = new HashSet<>();
        this.completed = false;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    public Set<Integer> getDependencies() {
        return dependencies;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

class TaskManager {
    private Map<Integer, Task> tasks;
    private PriorityQueue<Task> deadlineQueue;
    private PriorityQueue<Task> priorityQueue;

    public TaskManager() {
        tasks = new HashMap<>();
        deadlineQueue = new PriorityQueue<>(Comparator.comparing(Task::getDeadline));
        priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority).reversed());
    }

    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
        deadlineQueue.add(task);
        priorityQueue.add(task);
    }

    public void addDependency(int taskId, int dependencyId) {
        tasks.get(taskId).getDependencies().add(dependencyId);
    }

    public void completeTask(int taskId) {
        Task task = tasks.get(taskId);
        if (task.getDependencies().stream().allMatch(depId -> tasks.get(depId).isCompleted())) {
            task.setCompleted(true);
            System.out.println("Task " + taskId + " completed!");
        } else {
            System.out.println("Task " + taskId + " cannot be completed due to dependencies.");
        }
    }

    public int getHighestPriorityTask() {
        while (!priorityQueue.isEmpty()) {
            Task task = priorityQueue.poll();
            if (!task.isCompleted()) {
                return task.getTaskId();
            }
        }
        return -1; // No pending tasks
    }

    public void notifyOverdueTasks() {
        Date now = new Date();
        while (!deadlineQueue.isEmpty() && deadlineQueue.peek().getDeadline().before(now)) {
            Task task = deadlineQueue.poll();
            System.out.println("Task " + task.getTaskId() + " is overdue with deadline " + task.getDeadline() + "!");
        }
    }

    public void displayTasks() {
        System.out.println("Task List:");
        for (Task task : tasks.values()) {
            System.out.println("Task ID: " + task.getTaskId() + ", Title: " + task.getTitle() +
                    ", Deadline: " + task.getDeadline() + ", Priority: " + task.getPriority() +
                    ", Completed: " + task.isCompleted());
        }
    }
}

public class UserDefinedTaskManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        while (true) {
            System.out.println("1. Add Task");
            System.out.println("2. Add Dependency");
            System.out.println("3. Complete Task");
            System.out.println("4. Get Highest Priority Task");
            System.out.println("5. Notify Overdue Tasks");
            System.out.println("6. Display Tasks");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter task title: ");
                    String title = scanner.next();
                    System.out.print("Enter task description: ");
                    String description = scanner.next();
                    System.out.print("Enter task deadline (YYYY-MM-DD): ");
                    String deadlineStr = scanner.next();
                    Date deadline = parseDate(deadlineStr);
                    System.out.print("Enter task priority: ");
                    int priority = scanner.nextInt();

                    Task task = new Task(title, description, deadline, priority);
                    taskManager.addTask(task);
                    break;

                case 2:
                    System.out.print("Enter task ID: ");
                    int taskId = scanner.nextInt();
                    System.out.print("Enter dependency ID: ");
                    int dependencyId = scanner.nextInt();

                    taskManager.addDependency(taskId, dependencyId);
                    break;

                case 3:
                    System.out.print("Enter task ID to complete: ");
                    int completeTaskId = scanner.nextInt();
                    taskManager.completeTask(completeTaskId);
                    break;

                case 4:
                    int highestPriorityTask = taskManager.getHighestPriorityTask();
                    if (highestPriorityTask != -1) {
                        System.out.println("Highest priority task: " + highestPriorityTask);
                    } else {
                        System.out.println("No pending tasks.");
                    }
                    break;

                case 5:
                    taskManager.notifyOverdueTasks();
                    break;

                case 6:
                    taskManager.displayTasks();
                    break;

                case 0:
                    System.out.println("Exiting the task management system.");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Error parsing date. Using current date instead.");
            return new Date();
        }
    }
}