JC = javac
JVM = java
SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib
MAIN_CLASS = App
CLASSPATH = $(BIN_DIR):$(LIB_DIR)/mysql-connector-j-8.0.32.jar

SOURCES := $(shell find $(SRC_DIR) -name "*.java")
CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JC) -cp $(CLASSPATH) -d $(BIN_DIR) $


run: all
	$(JVM) -cp $(CLASSPATH) $(MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR)

setup-db:
	@echo "Creating database..."
	mysql -u root -p < sql/employeedb_create.sql
	@echo "Altering database schema..."
	mysql -u root -p < sql/employeedb_alter.sql
	@echo "Inserting initial data..."
	mysql -u root -p < sql/employeedb_insert.sql

insert-db:
	@echo "Inserting initial data..."
	mysql -u root -p < sql/employeedb_insert.sql

clear-db:
	@echo "Droping database employeedb..."
	mysql -u root -p < sql/employeedb_delete.sql

help:
	@echo "Available targets:"
	@echo "  all        - Compile all Java source files (default)"
	@echo "  run        - Compile and run the application"
	@echo "  clean      - Remove all compiled class files"
	@echo "  setup-db   - Set up the intial database schema and state"
	@echo "  clear-db   - Delete database"
	@echo "  help       - Display this help information"

.PHONY: all run clean setup-db alter-db help