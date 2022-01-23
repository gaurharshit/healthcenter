'use strict';

const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>
/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
class HealthCenter extends React.Component { // <1>

  constructor(props) {
    super(props);
    this.state = {appsDetails: [], formError: '', defaultTimer: 5, seconds: 5};
    this.onDelete = this.onDelete.bind(this);
    this.onStatusCheck = this.onStatusCheck.bind(this);
    this.onCreate = this.onCreate.bind(this);
    this.handleApplicationNameChange = this.handleApplicationNameChange.bind(
        this);
    this.handleHealthCheckUrlChange = this.handleHealthCheckUrlChange.bind(
        this);
    this.timer = 0;
    this.startTimer = this.startTimer.bind(this);
    this.countDown = this.countDown.bind(this);
  }

  startTimer() {
    setInterval(this.countDown, 1000);
  }

  countDown() {
    // Remove one second, set state so a re-render happens.
    let seconds = this.state.seconds - 1;
    this.setState({
      seconds: seconds,
    });

    // Check if we're at zero.
    if (seconds == 0) {
      this.loadFromServer();
      this.setState({seconds: this.state.defaultTimer});
    }

  }

  handleValidation() {
    let appId = this.state.appId;
    let url = this.state.healthCheckUrl;
    let formIsValid = true;

    if (!appId) {
      formIsValid = false;
      this.state.formError = "Cannot be empty";
    }

    if (!url) {
      formIsValid = false;
      this.state.formError = "Cannot be empty";
    }

    if (url !== "undefined") {
      if (!url.startsWith("http")) {
        formIsValid = false;
        this.state.formError = "Enter correct url";
      }
    }
    return formIsValid;
  }

  onDelete(appDetail) {
    client({
      method: 'DELETE',
      path: '/rest/api/v1/remove?appId=' + appDetail.appId
    }).done(response => {
          this.loadFromServer()
        }
    );
  }

  onStatusCheck(appDetail) {
    client({
      method: 'POST',
      path: '/rest/api/v1/application/checkStatus?appId=' + appDetail.appId
    }).done(response => {
          this.loadFromServer()
        }
    );
  }

  onCreate(event) {
    if (this.handleValidation()) {
      alert('A application is submitted with name: ' + this.state.appId
          + 'Url: ' + this.state.healthCheckUrl);
      event.preventDefault();
      const data = {
        "appId": this.state.appId,
        "healthCheckUrl": this.state.healthCheckUrl
      };
      const requestOptions = {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {'Content-Type': 'application/json'}

      }
      var s = JSON.stringify(data);
      fetch('http://localhost:8080/rest/api/v1/register', requestOptions).then(
          response => {
            this.loadFromServer()
          }
      );
    } else {
      alert("Error creating application:" + this.state.formError);
    }
  }

  handleApplicationNameChange(event) {
    this.setState({[event.target.name]: event.target.value})
  }

  handleHealthCheckUrlChange(event) {
    this.setState({[event.target.name]: event.target.value})
  }

  loadFromServer() {
    client({method: 'GET', path: '/rest/api/v1/applications'}).done(
        response => {
          this.setState({appsDetails: response.entity.externalAppStatus});
        });
  }

  componentDidMount() {
    this.startTimer();
    this.loadFromServer();
  }

  componentWillUnmount() {
    clearInterval(this.interval);
    clearInterval(this.timer);
  }

  render() {
    return (
        <div>
          <h1>
            <center>Health Center</center>
          </h1>

          <h2>Register Application</h2>

          <form onSubmit={this.onCreate}>
            <label>
              Application Name:
              <input name="appId" type="text"
                     value={this.state.applicationName}
                     onChange={this.handleApplicationNameChange}/>
            </label>
            <label>
              Health Check URL :
              <input name="healthCheckUrl" type="text"
                     value={this.state.healthUrl}
                     onChange={this.handleHealthCheckUrlChange}/>
            </label>
            <input type="submit" value="Register"/>
          </form>


          <h2>Health Status
          </h2>
          <center>scheduled check in: {this.state.seconds}</center>
          <AppList appsDetails={this.state.appsDetails}
                   onDelete={this.onDelete}
                   onStatusCheck={this.onStatusCheck}/>

        </div>

    )
  }
}

class AppList extends React.Component {
  render() {
    const appInfo = this.props.appsDetails.map(appDetail =>
        <AppInfo key={appDetail.appId} appDetails={appDetail}
                 appUpdateDetails={appDetail}
                 onDelete={this.props.onDelete}
                 onStatusCheck={this.props.onStatusCheck}
        />
    );
    return (
        <table>
          <tbody>
          <tr>
            <th>Application Name</th>
            <th>Health Check URL</th>
            <th>Status</th>
            <th>Creation Time</th>
            <th>Poll Time</th>
          </tr>
          {appInfo}
          </tbody>
        </table>
    )
  }
}

class AppInfo extends React.Component {
  constructor(props) {
    super(props);
    console.log(this.props)
    this.handleDelete = this.handleDelete.bind(this);
    this.handleCheckStatus = this.handleCheckStatus.bind(this);
  }

  handleDelete() {
    this.props.onDelete(this.props.appDetails);
  }

  handleCheckStatus() {
    this.props.onStatusCheck(this.props.appUpdateDetails);
  }

  render() {
    return (
        <tr>
          <td>{this.props.appDetails.appId}</td>
          <td>{this.props.appDetails.url}</td>
          <td>{this.props.appDetails.status}</td>
          <td>{this.props.appDetails.creationTime}</td>
          <td>{this.props.appDetails.pollTime}</td>
          <td>
            <button onClick={this.handleCheckStatus}>Check Status</button>
          </td>
          <td>
            <button onClick={this.handleDelete}>Delete</button>
          </td>
        </tr>
    )
  }
}

ReactDOM.render(
    <HealthCenter/>,
    document.getElementById('react')
)

