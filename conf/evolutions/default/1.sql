# --- !Ups
CREATE TABLE exams (
  exam_id VARCHAR(255) PRIMARY KEY,
  subject VARCHAR(255) NOT NULL,
  due_date DATETIME NOT NULL,
  evaluation_status VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
);

CREATE TABLE exam_results (
  exam_result_id VARCHAR(255) PRIMARY KEY,
  exam_id VARCHAR(255) NOT NULL,
  score INT NOT NULL,
  student_id VARCHAR(255) NOT NULL,
  evaluation VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  CONSTRAINT fk_exam FOREIGN KEY (exam_id) REFERENCES exams(exam_id)
);

# --- !Downs
DROP TABLE IF EXISTS exam_results;
DROP TABLE IF EXISTS exams;
