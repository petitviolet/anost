export interface State {
  loading: boolean;  // duration of sending request
  error: Error | null; // something error occurred or not
}
