DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
	project_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	project_name VARCHAR(128) NOT NULL,
	estimated_hours DECIMAL(7,2),
	actual_hours DECIMAL(7,2),
	difficulty INT,
	notes TEXT
);

CREATE TABLE category (
	category_id INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	category_name VARCHAR(128) NOT NULL
);

CREATE TABLE step(
	step_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	project_id INT NOT NULL,
	step_text TEXT,
	step_order INT,
	FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE
);

CREATE TABLE material (
	material_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	project_id INT NOT NULL,
	material_name VARCHAR(128) NOT NULL,
	num_required INT,
	cost DECIMAL(7,2),
	FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE
);

CREATE TABLE project_category (
	project_id INT NOT NULL,
	category_id INT NOT NULL,
	FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE,
	FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE
);

-- adding data
INSERT INTO project (project_name, estimatedHours, actual_hours, difficulty, notes) VALUES ("create a hamster", 14, 3, 5, "some test data");
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, "Yarn", 1, 45.99);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, "chimken", 4, 3.49);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, "crochet a circle", 1);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, "stuff with polyfill", 2);
INSERT INTO category (category_id, category_name) VALUES (1, "crochet");
INSERT INTO project_category (project_id, category_id) VALUES (1, 1);