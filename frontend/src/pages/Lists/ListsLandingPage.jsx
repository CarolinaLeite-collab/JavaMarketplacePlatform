import { DefaultLayout } from "../../components/layout/DefaultLayout.tsx";
import { Button, Stack, Container } from "@mantine/core";
import { useNavigate } from "react-router-dom";

export default function ListsLandingPage() {
    const navigate = useNavigate();

    return (
        <DefaultLayout title="Lists" subtitle="CHOOSE AN OPTION:">
            <Container size="sm">
                <Stack align="center" mt="xl">
                    <Button
                        size="lg"
                        fullWidth
                        radius="xl"
                        variant="filled"
                        color="var(--mantine-color-indigo-7)"
                        onClick={() => navigate("/my-lists")}
                    >
                        My Lists
                    </Button>

                    <Button
                        size="lg"
                        fullWidth
                        radius="xl"
                        variant="filled"
                        color="var(--mantine-color-indigo-7)"
                        onClick={() => navigate("/lists/public")}
                    >
                        Public Lists
                    </Button>
                </Stack>
            </Container>
        </DefaultLayout>
    );
}



