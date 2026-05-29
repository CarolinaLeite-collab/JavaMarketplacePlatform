import { Title, Text } from '@mantine/core';

export function PageTitle({ children, subtitle }) {
    return (
        <>
            <Title
                order={1}
                ta="center"
                fz={72}
                fw={400}
                style={{ fontFamily: 'EB Garamond, serif' }}
            >
                {children}
            </Title>
            {subtitle && (
                <Text ta="center" mb="xl" mt="lg" fz="sm" c="dimmed">
                    {subtitle}
                </Text>
            )}
        </>
    );
}