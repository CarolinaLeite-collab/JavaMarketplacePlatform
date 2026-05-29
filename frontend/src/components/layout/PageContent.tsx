import { Container, Stack } from '@mantine/core';
import { PageTitle } from "./PageTitle.tsx";

export function PageContent({ title, children }) {
    return (
        <Container size="lg" py="xl" my="xl">
            <Stack gap="md">
                {title && <PageTitle>{title}</PageTitle>}
                {children}</Stack>
        </Container>
    );
}