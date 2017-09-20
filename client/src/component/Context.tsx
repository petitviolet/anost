import * as React from 'react';
import { State } from '../module/state';

export const Context: React.StatelessComponent<{ value: State }> =
  (props: { value: State }) => {
    // const user = this.props.value.user;
    return (
      <div>
        {(props.value.loading === true) ? <p> loading... </p> : null}
        {(props.value.error) ? <p> error! {props.value.error.message} </p> : null}
      </div>
    );
  };