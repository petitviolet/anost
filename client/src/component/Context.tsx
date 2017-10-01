import * as React from 'react';
import { State } from '../module/state';

export const Context: React.StatelessComponent<{ value: State }> =
  (props: { value: State }) => {
    const { value } = props;
    console.log('context', props);
    if (!value) { return null; }
    return (
      <div>
        {(value.loading === true) ? <p> loading... </p> : null}
        {(value.error) ? <p> error! {value.error.message} </p> : null}
      </div>
    );
  };