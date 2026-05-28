import { axe, render } from '@/test-utils';
import attributes from './attributes.json';
import { Header } from '../components/header/Header';

describe('Header', () => {
    axe([<Header key="1" {...(attributes as any)} />]);

    it('renders correctly', () => {
        render(<Header {...(attributes as any)} />);
    });
});