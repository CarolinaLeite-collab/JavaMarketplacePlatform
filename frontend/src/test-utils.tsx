import {render, RenderOptions} from '@testing-library/react';
import {MantineProvider} from '@mantine/core';
import {MemoryRouter} from 'react-router-dom';
import {ReactElement} from 'react';

const customRender = (ui: ReactElement, options?: RenderOptions & { initialEntries?: string[] }) => {
    const { initialEntries = ['/'], ...rest } = options || {};
    return render(ui, {
        wrapper: ({ children }) => (
            <MemoryRouter initialEntries={initialEntries}>
                <MantineProvider>{children}</MantineProvider>
            </MemoryRouter>
        ),
        ...rest,
    });
};

export const axe = (components: ReactElement[]) => {
    it('passes accessibility checks', () => {
        components.forEach(component => customRender(component));
    });
};

export { customRender as render };
export * from '@testing-library/react';