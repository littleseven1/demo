package com.example.project.entity;

public class UserViewData {
        private int user_id;
        private int number;
        private int category;

        public UserViewData(int user_id, int number, int category) {
                this.user_id = user_id;
                this.number = number;
                this.category = category;
        }

        public int getUserId() {
                return user_id;
        }

        public void setUserId(int user_id) {
                this.user_id = user_id;
        }

        public int getNumber() {
                return number;
        }

        public void setNumber(int number) {
                this.number = number;
        }

        public int getCategory() {
                return category;
        }

        public void setCategory(int category) {
                this.category = category;
        }

}
