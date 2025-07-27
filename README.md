# pa-web-server

A multithreaded and concurrent **Java Web Server**, built to serve static HTML content from a directory structure while ensuring **parallel access control**, **thread synchronization**, and **asynchronous logging**.

Developed for the **Advanced Programming (Programação Avançada)** course, this project demonstrates mastery of parallel programming in Java using threads, semaphores, locks, and architectural concurrency patterns.

---

## ⚙️ Features

- Serves `.html` files over HTTP from a root directory (including subdirectories)
- Handles **up to N concurrent requests** (defined in `server.config`)
- Uses **Semaphores, Locks, and Threads/Runnables**
- Enforces **First-Come, First-Served (FCFS)** policy per file to prevent race conditions
- Returns `index.html` when a file isn’t specified
- Responds with `404 Not Found` if the requested or default file doesn’t exist
- Generates **structured asynchronous logs** in JSON format

---

## 🔧 Technologies Used

- **Java Threads** – for handling multiple HTTP requests  
- **Semaphores & Locks** – for access control and resource synchronization  
- **ExecutorService** – optional (thread pooling)  
- **Concurrent Queues** – to ensure orderly access to files  
- **Java I/O & Networking** – to handle socket and file operations  
- **Javadoc** – for documentation  
- **JaCoCo** – for code coverage reports  
- **GitHub Actions** – for CI/CD (testing, coverage, docs)

## 🔄 Request Handling Workflow

1. Server reads `server.config` and listens on configured port.  
2. On receiving a connection:
   - Acquires a slot from a **Semaphore** (max N threads).
   - Creates a **Runnable** to handle the request.
   - If file is being accessed by another thread, it's placed in a **queue** (FCFS).
3. Returns:
   - HTML file if found.
   - `index.html` if no file specified.
   - `404 Not Found` if neither exist.
4. Each request is **logged asynchronously** to `server.log`.

---

## 🧪 Testing and CI

- Unit tests located under `src/test/java/`
- Code coverage via **JaCoCo**
- Continuous integration via **GitHub Actions** runs:
  - All tests
  - Generates documentation (`Javadoc`)
  - Creates coverage report

---

## 🛠 Configuration (`server.config`)

```ini
server.port=8080
server.host=127.0.0.1
server.max-conections=5

# Timeout Configuration (in miliseconds)

server.connection-timeout=10000
```
## 🧠 Academic Honesty

This project abides by the highest standards of academic integrity.

All ideas, algorithms, and code not originally authored by the project contributors are explicitly credited to their respective sources. Any form of plagiarism — including but not limited to copying code from classmates, online repositories, or third-party sources without proper citation — is strictly prohibited.

It is the responsibility of each contributor to ensure their code is original or appropriately attributed. Misconduct may result in academic penalties according to institutional policies.

---

## 👨‍💻 Author Note

This project was developed as part of the *Programação Avançada* (Advanced Programming) course — 2024/2025 edition — and serves as a practical introduction to concurrent server programming in Java.

It aims to strengthen key concepts in:

- Thread and resource management
- Synchronization using locks and semaphores
- Asynchronous system logging
- Scalable and secure server architecture

The experience gained here is foundational for real-world back-end development and future studies in high-performance systems and cybersecurity.

