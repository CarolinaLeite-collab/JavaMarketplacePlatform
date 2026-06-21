import {Text, Title} from '@mantine/core';

export function PageTitle({subtitle, children }: { subtitle?: string; children: React.ReactNode }) {
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