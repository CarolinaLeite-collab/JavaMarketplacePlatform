import { Title } from '@mantine/core';

export function PageTitle({ children }) {
    return (
        <Title
            order={1}
            ta="center"
            mb="xl"
            fz={72}
            fw={400}
            style={{ fontFamily: 'EB Garamond, serif' }}
        >
            {children}
        </Title>
    );
}