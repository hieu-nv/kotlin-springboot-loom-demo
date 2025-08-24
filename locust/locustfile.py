from locust import HttpUser, task


class HelloWorldUser(HttpUser):
    @task
    def hello_world(self):
        self.client.post(
            "/blocking/users?delay=200",
            json={
                "id": None,
                "userName": "Jack",
                "email": "Rabbit@hi.nl",
                "avatarUrl": None,
            },
        )
