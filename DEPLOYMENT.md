# US080 — Secure Deployment to the DEI Cloud (Docker Compose)

> *As a PO, I want the application to be securely deployed in the DEI cloud environment using
> Docker Compose, with documented configuration, exposed services, and required environment
> variables, so that the delivery can be reproduced, validated, and audited.*

This runbook is the auditable record of the MiteLovers (Team B) deployment. Following it top to
bottom reproduces the deployment from scratch. Every command below is one that was actually run;
the addresses, ports and tags are the real ones used.

The application is **publicly exposed over HTTPS** through the DEI gateway: the frontend and the
backend are each mapped to a gateway HTTPS port. A development-only SSH-tunnel method is also
documented in §9 as a fallback.

---

## 0. Infrastructure summary

| Item                | Value                                                                 |
|---------------------|-----------------------------------------------------------------------|
| VM                  | `vs266` — Alpine Linux 3.20, template **86** (fresh install w/ Docker), QEMU/KVM |
| VM internal IP      | `10.9.21.10/16` (VNET1, only reachable inside DEI / via VPN)           |
| SSH (external)      | `vsgate-ssh.dei.isep.ipp.pt:10266` → VM port `2222`                    |
| Frontend (public)   | `https://vs-gate.dei.isep.ipp.pt:10266` → VM port `2226` → container `8080` |
| Backend (public)    | `https://vs-gate.dei.isep.ipp.pt:30266` → VM port `2228` → container `8081` |
| Registry (Harbor)   | `vs-gate.dei.isep.ipp.pt:10684`                                        |
| Harbor project      | `devsecops-grupo-b`                                                    |
| Harbor user         | `switchb`                                                              |
| Image tag in use    | `local-test` (replace with the short commit SHA for the final delivery — see §7) |

**Architecture in one line:** three containers (H2 server + Spring Boot backend + nginx/React
frontend) orchestrated by Docker Compose. The React UI calls the Spring Boot REST API **directly
from the browser** (no reverse proxy). Both app services are published on VM ports that the DEI
gateway exposes as **public HTTPS** endpoints; the gateway terminates TLS and forwards plain HTTP
to the VM. The database is a **separate H2 container in TCP server mode**, with a named volume for
persistence.

```
Browser ──https──> :10266 (gateway) ──http──> VM:2226 ──> frontend container :8080 (nginx, React build)
   │
   └─────https──> :30266 (gateway) ──http──> VM:2228 ──> backend container :8081 (Spring Boot REST API)
                                                              │
                                                              └──tcp://db:9092──> db (H2 server, named volume)
```

| Service  | Image (Harbor)                                  | VM port → container | Public URL                                  |
|----------|-------------------------------------------------|---------------------|---------------------------------------------|
| db       | `…/devsecops-grupo-b/mitelovers-h2:<tag>`       | 9092 (TCP, internal)| — (internal only)                           |
| backend  | `…/devsecops-grupo-b/mitelovers-backend:<tag>`  | 2228 → 8081         | `https://vs-gate.dei.isep.ipp.pt:30266`     |
| frontend | `…/devsecops-grupo-b/mitelovers-frontend:<tag>` | 2226 → 8080         | `https://vs-gate.dei.isep.ipp.pt:10266`     |

> **Gateway port mappings (from the DEI panel, "Public access static TCP port mappings"):** the VM
> only exposes ports 2222–2229, bridged by the gateway. The HTTPS-capable ones are
> `2226 ← :10266`, `2228 ← :30266`, `2229 ← :40266`. We use 2226 (frontend) and 2228 (backend);
> 2229 is spare. The notation `(HTTP) … ← (HTTPS)` in the panel means the **gateway terminates
> TLS** and talks plain HTTP to the VM — so the containers themselves serve HTTP, not HTTPS.

---

## 1. Create the VM (DEI cloud panel)

In the DEI Virtual Servers panel, create a VM from template **86 — "Alpine Linux 3.20.0 (fresh
install with Docker)"**. This template ships with Docker already installed but **without an SSH
server**, so the first configuration is done through the **noVNC web console** (root login; the
initial root password is shown in the DEI panel for this VM).

> Template 86 (QEMU/KVM, full VM) was chosen over Sysbox-based templates to avoid the
> Docker-in-Docker complications of running the stack inside a container-as-VM.

After creating the VM, in the panel:
- **Enable noVNC console access** (used for the initial setup; disable it again when done).
- Note the **Public access static TCP port mappings** (the gateway ports listed in §0).
- The VM/account is **individual** (the panel owner). Teammates do not create or control the VM
  from the panel; they access the running VM over SSH (§2.1) and reproduce the deployment (§6).

---

## 2. Prepare the VM (run in the noVNC console)

These commands install the SSH server and Docker Compose, configure SSH on port **2222**, and make
both SSH and Docker start automatically on boot.

```sh
apk update
apk add openssh-server
apk add docker-compose          # installs the modern compose plugin; `docker compose` (with a space) works

# SSH: listen on 2222 (the gateway-mapped port) and allow root login
echo "Port 2222" >> /etc/ssh/sshd_config
echo "PermitRootLogin yes" >> /etc/ssh/sshd_config

# Allow SSH port-forwarding (needed for the dev tunnel in §9).
# IMPORTANT: the stock config already contains a line "AllowTcpForwarding no" near the top, and
# SSH honours the FIRST occurrence — so appending "yes" at the end is not enough. Edit the
# existing line instead:
sed -i 's/^AllowTcpForwarding no/AllowTcpForwarding yes/' /etc/ssh/sshd_config

# Enable + start both services so they survive a reboot
rc-update add sshd && rc-service sshd start
rc-update add docker && rc-service docker start
```

Verify (all confirmed):

```sh
docker --version                          # Docker 26.1.3
docker compose version                    # v2.27.0
docker ps                                 # daemon responds (empty table)
rc-update show | grep -E "sshd|docker"    # both in runlevel "default" (persist across reboot)
grep -n -i allowtcpforwarding /etc/ssh/sshd_config   # the active line must read "yes"
```

> **Persistence note:** template 86 is a real (QEMU/KVM) VM, so the disk persists — packages
> installed with `apk add` stay. Services only auto-start after reboot if registered with
> `rc-update add`, which is why both `sshd` and `docker` are added to the default runlevel.

> **Password note:** the initial root password (shown in the DEI panel) is re-enforced by the
> panel on every VM start, so changing it from inside the VM has no lasting effect.

Confirm external SSH access works **from a dev machine** (no VPN required — see §8):

```bash
ssh -p 10266 root@vsgate-ssh.dei.isep.ipp.pt      # root password from the DEI panel
```

---

## 2.1. SSH key access (one-time, per team member)

So that nobody has to type — or share — the VM password during normal work, **each team member
installs their own SSH public key on the VM**. After this, they log in (and run the deployment)
without a password.

> **Why per-person keys, not a shared one:** an SSH key is a *pair* — a private key that never
> leaves your computer, and a public key that is safe to share. Each member keeps their own private
> key and installs only their public key on the VM. Nobody shares a private key, and the password
> stops being passed around. The VM ends up trusting every member individually.

Each member runs the following **on their own computer**:

**Step 1 — check whether you already have a key.**
```bash
ls ~/.ssh/id_ed25519.pub
```
`~/.ssh/` is the hidden folder where SSH keeps your keys; `id_ed25519.pub` is the **public** half
of an ed25519 key pair (the matching private key `id_ed25519`, no extension, stays on your machine).
- If it prints the file → you already have a key, go to Step 2.
- If it prints `No such file or directory` → generate one: `ssh-keygen -t ed25519` (Enter for
  defaults; passphrase optional).

**Step 2 — install your public key on the VM** (the only time you need the password):
```bash
ssh-copy-id -p 10266 root@vsgate-ssh.dei.isep.ipp.pt    # root password from the DEI panel (last time)
```

**Step 3 — verify you can log in without a password:**
```bash
ssh -p 10266 root@vsgate-ssh.dei.isep.ipp.pt
```
A `vs:~#` prompt with no password prompt means your key works. From now on every SSH / `scp` /
`DOCKER_HOST=ssh` to the VM uses your key automatically.

> **Reboot caveat:** depending on how template 86 persists `~/.ssh/authorized_keys`, the installed
> keys *may not survive a VM reboot*. If, after a reboot, the VM asks for a password again, re-run
> Step 2.

---

## 3. Application configuration (baked into the images)

Datasource, credentials and CORS origin are **externalised to environment variables** so the same
image works locally and on the VM; only the env values change.

```properties
# H2 — file-based default for dev/tests, tcp:// injected in production/VM
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:h2:file:./data/miteloversdb;DB_CLOSE_ON_EXIT=FALSE}
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:sa}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}

# JPA / Hibernate — "update" so data survives restarts (persistence via the volume)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

spring.profiles.active=bootstrap,jpa
server.port=8081

# CORS — env var overrides in production, localhost defaults for dev
cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:5173,http://localhost:8080}

# Actuator — health endpoint only (DevSecOps: reduce exposed surface)
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=never

# SpringDoc / Swagger — disabled (DevSecOps: reduce exposed surface)
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

The seeding step (`DataInitializer`) has an **idempotency guard** and, with `ddl-auto=update` plus
the named volume, the data is seeded once and survives container restarts/recreation.

### 3.1. HATEOAS links behind the HTTPS gateway (critical)

The backend builds HATEOAS links with `WebMvcLinkBuilder.linkTo(methodOn(...))`, which derives the
scheme/host/port from the **incoming request**. Because the gateway terminates TLS and forwards
plain HTTP to the VM, the backend would otherwise generate links as `http://…:8081` — and the
browser, having loaded the page over HTTPS, **blocks those as mixed content**.

The fix is to make Spring honour the gateway's `X-Forwarded-*` headers, so links are generated with
the original public scheme/host/port (`https://vs-gate.dei.isep.ipp.pt:30266`). This is set via an
environment variable on the backend (no code change, no rebuild):

```
SERVER_FORWARD_HEADERS_STRATEGY=framework
```

> Confirmed working: the DEI gateway **does** send `X-Forwarded-*`, so `framework` is enough and
> the hrefs come back as `https://…:30266`.

---

## 4. Why the images are multi-platform (amd64 + arm64)

The development machine is Apple Silicon (**arm64**); the DEI VM is **amd64**. Images built only
for arm64 fail on the VM at pull time with `no matching manifest for linux/amd64`. The fix is to
build **multi-platform** images (arm64 + amd64) and publish them to Harbor as a single manifest
list; the VM pulls the amd64 variant automatically. Hence `docker buildx` with
`--platform linux/amd64,linux/arm64` instead of a plain `docker build`.

> A multi-platform build cannot be loaded into the local Docker daemon; it must be pushed straight
> to the registry. That is why the build commands end in `--push` and require being logged in to
> Harbor first.

---

## 5. Build & push the images (run on the dev machine)

```bash
cd <path>/switch-project-team_b

# 1. Authenticate to Harbor (credentials provided by the DevSecOps lecturer)
docker login vs-gate.dei.isep.ipp.pt:10684        # user: switchb

# 2. Create a buildx builder that supports multi-platform builds.
docker buildx create --name multiarch --driver docker-container --use
docker buildx inspect --bootstrap                 # confirms linux/amd64 + linux/arm64

# 3. Pick the tag. For this run we used "local-test"; for the final delivery use the commit SHA:
export APP_VERSION=local-test
# export APP_VERSION=$(git rev-parse --short HEAD)   # ← preferred for traceability (US076)
export REG=vs-gate.dei.isep.ipp.pt:10684/devsecops-grupo-b

# 4. Build + push each image, multi-platform.

# backend
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t $REG/mitelovers-backend:$APP_VERSION \
  -f Dockerfile \
  --push .

# frontend — VITE_API_URL is a BUILD ARG: Vite inlines it into the static bundle at build time.
# For public exposure the browser reaches the backend at its PUBLIC HTTPS URL, so that is the value
# baked in. Use --no-cache: changing only the build-arg can otherwise reuse a cached "npm run build"
# layer and silently keep the previous URL (the bundle hash would not change).
docker buildx build \
  --no-cache \
  --platform linux/amd64,linux/arm64 \
  --build-arg VITE_API_URL=https://vs-gate.dei.isep.ipp.pt:30266 \
  -t $REG/mitelovers-frontend:$APP_VERSION \
  -f frontend/Dockerfile \
  --push ./frontend

# h2 (database server image: oscarfonts/h2 + netcat for the healthcheck)
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t $REG/mitelovers-h2:$APP_VERSION \
  -f h2/Dockerfile \
  --push ./h2

# 5. Confirm the manifest list contains BOTH platforms
docker buildx imagetools inspect $REG/mitelovers-backend:$APP_VERSION
#   → expect:  Platform: linux/amd64   and   Platform: linux/arm64
```

> **Tip — confirming VITE_API_URL really took:** after building, the frontend bundle filename
> (`index-XXXX.js`) changes whenever the inlined URL changes. If the hash is identical to the
> previous build, the build arg did NOT take — rebuild with `--no-cache`.

Record the `APP_VERSION` used — for the final delivery it is the audit link to the source commit
(US076).

---

## 6. Deploy on the DEI VM

The compose file used on the VM is **`docker-compose.prod.yml`** (copied to the VM as
`docker-compose.yml`). It differs from a local compose in that the `db` service has **no `build:`**
(the VM only pulls from Harbor), the app services are published on the **gateway ports**
(`2226`/`2228`), `CORS_ALLOWED_ORIGINS` is the **public frontend origin**, and the backend has
`SERVER_FORWARD_HEADERS_STRATEGY=framework`. The full file is in §9.

```bash
# --- on the dev machine: copy the compose file to the VM ---
scp -P 10266 docker-compose.prod.yml root@vsgate-ssh.dei.isep.ipp.pt:~/docker-compose.yml
# no password if your SSH key is installed (§2.1); otherwise use the root password from the DEI panel

# --- on the VM (over SSH) ---
ssh -p 10266 root@vsgate-ssh.dei.isep.ipp.pt        # no password with SSH key (§2.1)

docker login vs-gate.dei.isep.ipp.pt:10684          # user: switchb
export APP_VERSION=local-test
docker compose pull                                  # pulls the amd64 variants
docker compose up -d
```

> **Alternative (remote deploy from the dev machine):** with your SSH key installed you can drive
> the VM's Docker daemon directly, without copying files or opening a shell on the VM:
> ```bash
> export APP_VERSION=local-test
> DOCKER_HOST="ssh://root@vsgate-ssh.dei.isep.ipp.pt:10266" docker compose -f docker-compose.prod.yml pull
> DOCKER_HOST="ssh://root@vsgate-ssh.dei.isep.ipp.pt:10266" docker compose -f docker-compose.prod.yml up -d
> ```
> (requires the SSH key from §2.1 — `DOCKER_HOST=ssh` cannot prompt for a password)

`up -d` starts `db` first; the `backend` waits until `db` is **healthy**, and the `frontend` waits
until the `backend` is **healthy**. The first start takes ~80s because of the health `start_period`s.

```sh
docker compose ps
# db        Up (healthy)
# backend   Up (healthy)   0.0.0.0:2228->8081/tcp
# frontend  Up (healthy)   0.0.0.0:2226->8080/tcp
```

---

## 7. Validation

```sh
# --- on the VM ---
docker compose ps                                   # all three "healthy"
wget -qO- http://localhost:8081/actuator/health     # {"status":"UP"}
docker compose logs backend | grep -i hikari        # url=jdbc:h2:tcp://db:9092/miteloversdb
```

**Public validation (from any machine, any network, no VPN):**

```bash
# backend health, public HTTPS
curl -k https://vs-gate.dei.isep.ipp.pt:30266/actuator/health    # {"status":"UP"}
```

Then open the UI in a browser:

```
https://vs-gate.dei.isep.ipp.pt:10266
```

Confirm the Marketplace / Library render with data. In the browser dev-tools Console the API calls
(including HATEOAS `getByHref` calls) must all target **`https://vs-gate.dei.isep.ipp.pt:30266`** —
no `http://` and no `localhost`. If you still see the old URLs, the browser is serving a cached
bundle: use "Empty Cache and Hard Reload" (and confirm the VM pulled the new image).

> **If the browser shows "Mixed Content … blocked":** the backend is generating `http://` links —
> confirm `SERVER_FORWARD_HEADERS_STRATEGY=framework` is set on the backend (§3.1) and that the
> frontend was built with the `https://` API URL (§5).
> **If a healthcheck never goes healthy:** confirm the tool used by the check exists in the image
> (`nc` for db, `wget` for backend/frontend).

---

## 8. Network exposure & no-VPN access (note for the defence)

The VM's internal address (`10.9.21.10` / `vs266.dei.isep.ipp.pt`) is only reachable from inside
the DEI network or over VPN. We never reach it directly. The DEI **public gateway** bridges from
the internet to the VM's mapped ports — SSH (`vsgate-ssh:10266` → 2222) and the HTTPS app ports
(`vs-gate:10266` → 2226, `vs-gate:30266` → 2228). Enabling external access in the panel is what
opens those public doors, replacing the need for a VPN. The gateway terminates TLS, so the
containers serve plain HTTP internally.

This is controlled, limited exposure: only the gateway-mapped ports (2222–2229) are reachable
externally; the internal IP and all other ports stay protected behind the DEI network / VPN.

### 8.1. Operational security

- **Root login over a public port with a weak default password is the main risk.** Mitigations:
  per-member **SSH keys** (§2.1) instead of the password; and **disabling external access in the
  panel when the VM is not in use**.
- **Stop the VM when not in use** (panel) — zero exposure and saves credits.
- **Disable the noVNC console** when not actively configuring the VM.

---

## 9. Development fallback — SSH tunnel (no public exposure)

Before public exposure was configured, the app was validated via an SSH tunnel. This is still
useful for development or if the gateway is unavailable. With the tunnel, the app runs on the VM on
ports 8080/8081 (not the gateway ports), and the frontend image must be built with
`VITE_API_URL=http://localhost:8081`.

```bash
ssh -p 10266 -L 8080:localhost:8080 -L 8081:localhost:8081 root@vsgate-ssh.dei.isep.ipp.pt
# leave open; then browse http://localhost:8080
```

> Requires `AllowTcpForwarding yes` on the VM (§2). If the tunnel prints
> `channel N: open failed: administratively prohibited`, fix the FIRST `AllowTcpForwarding` line to
> `yes` and `rc-service sshd restart`.
> Note: the tunnel and public-exposure modes need **different** frontend builds (different
> `VITE_API_URL`) and different `CORS_ALLOWED_ORIGINS` — they are not interchangeable without a rebuild.

---

## 10. `docker-compose.prod.yml` (the file deployed on the VM)

```yaml
services:

  db:
    image: vs-gate.dei.isep.ipp.pt:10684/devsecops-grupo-b/mitelovers-h2:${APP_VERSION:?set APP_VERSION}
    environment:
      H2_OPTIONS: "-tcp -tcpAllowOthers -tcpPort 9092 -baseDir /opt/h2-data -ifNotExists"
    volumes:
      - mitelovers-data:/opt/h2-data
    restart: unless-stopped
    security_opt:
      - no-new-privileges:true
    cap_drop:
      - ALL
    # NOTE: resource limits below require Swarm; ignored by `docker compose up`.
    # Kept as documentation of intended limits for a future orchestrated deployment.
    deploy:
      resources:
        limits:
          cpus: "0.50"
          memory: "256M"
    healthcheck:
      test: [ "CMD-SHELL", "nc -z localhost 9092 || exit 1" ]
      interval: 15s
      timeout: 5s
      retries: 5
      start_period: 20s

  backend:
    image: vs-gate.dei.isep.ipp.pt:10684/devsecops-grupo-b/mitelovers-backend:${APP_VERSION:?set APP_VERSION}
    ports:
      - "2228:8081"
    environment:
      SERVER_FORWARD_HEADERS_STRATEGY: "framework"
      SPRING_PROFILES_ACTIVE: "bootstrap,jpa"
      CORS_ALLOWED_ORIGINS: "https://vs-gate.dei.isep.ipp.pt:10266"
      SPRING_DATASOURCE_URL: "jdbc:h2:tcp://db:9092/miteloversdb;DB_CLOSE_ON_EXIT=FALSE"
      JAVA_TOOL_OPTIONS: "-XX:MaxRAMPercentage=75.0"
    depends_on:
      db:
        condition: service_healthy
    restart: unless-stopped
    security_opt:
      - no-new-privileges:true
    cap_drop:
      - ALL
    deploy:
      resources:
        limits:
          cpus: "0.50"
          memory: "512M"
    healthcheck:
      test: ["CMD", "wget", "--spider", "-q", "http://localhost:8081/actuator/health"]
      interval: 30s
      timeout: 5s
      retries: 3
      start_period: 60s

  frontend:
    image: vs-gate.dei.isep.ipp.pt:10684/devsecops-grupo-b/mitelovers-frontend:${APP_VERSION:?set APP_VERSION}
    ports:
      - "2226:8080"
    depends_on:
      backend:
        condition: service_healthy
    restart: unless-stopped
    security_opt:
      - no-new-privileges:true
    cap_drop:
      - ALL
    deploy:
      resources:
        limits:
          cpus: "0.25"
          memory: "128M"
    healthcheck:
      test: ["CMD", "wget", "--spider", "-q", "http://127.0.0.1:8080/"]
      interval: 30s
      timeout: 5s
      retries: 3
      start_period: 10s

volumes:
  mitelovers-data:

```

---

## 11. Required environment variables

| Variable                        | Where         | Value (this deployment)                          | Purpose                                       |
|---------------------------------|---------------|--------------------------------------------------|-----------------------------------------------|
| `APP_VERSION`                   | build + VM    | `local-test` (final: short git SHA)              | Image tag; traceability to commit (US076)     |
| `VITE_API_URL`                  | **build arg** | `https://vs-gate.dei.isep.ipp.pt:30266`          | Public backend URL, baked into the frontend   |
| `CORS_ALLOWED_ORIGINS`          | backend env   | `https://vs-gate.dei.isep.ipp.pt:10266`          | Allows the public frontend origin to call the API |
| `SERVER_FORWARD_HEADERS_STRATEGY` | backend env | `framework`                                      | Make HATEOAS links use the public https scheme/host (§3.1) |
| `SPRING_DATASOURCE_URL`         | backend env   | `jdbc:h2:tcp://db:9092/miteloversdb;DB_CLOSE_ON_EXIT=FALSE` | Connect to the H2 server container by TCP |
| `SPRING_PROFILES_ACTIVE`        | backend env   | `bootstrap,jpa`                                  | Enables JPA persistence + data seeding        |
| `JAVA_TOOL_OPTIONS`             | backend env   | `-XX:MaxRAMPercentage=75.0`                      | JVM heap sizing under the memory limit        |
| `H2_OPTIONS`                    | db env        | `-tcp -tcpAllowOthers -tcpPort 9092 -baseDir /opt/h2-data -ifNotExists` | H2 server mode + data dir on the volume |

> **Critical:** `VITE_API_URL` is a **build-time** value. Vite inlines it into the JS bundle, so it
> is fixed inside the frontend image and cannot be changed at runtime — rebuild + re-push to change it.

---

## 12. Security controls in place (DevSecOps mapping)

- **Public access over HTTPS only:** both services are reached via the gateway's TLS-terminated
  HTTPS ports; plain-HTTP links are eliminated via forwarded-header handling (§3.1).
- **Non-root / least privilege:** every service runs with `no-new-privileges:true` and
  `cap_drop: ALL`; the backend/frontend images run as unprivileged users.
- **Reduced surface:** Swagger/OpenAPI disabled; Actuator exposes only `/health` with no details.
- **Pinned base images** (by digest) in the Dockerfiles → reproducible builds (US072).
- **Private registry:** images pulled from an authenticated Harbor project.
- **Controlled external access:** only gateway-mapped ports are reachable; access can be disabled
  in the panel when idle (§8.1).
- **Image traceability:** images tagged with `APP_VERSION` (short commit SHA for the final delivery),
  linking running containers to the source (US076).

---

## 13. Documented decisions & accepted risks (US078)

| Decision                                                    | Rationale / accepted risk                                                                                          |
|-------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| Public exposure via **two gateway HTTPS ports** (not a proxy) | The lecturer offered two valid options (single nginx proxy vs two ports) and said to pick the easiest. Two ports avoided changing the frontend's HATEOAS href-following logic. |
| `SERVER_FORWARD_HEADERS_STRATEGY=framework`                 | Gateway terminates TLS; without this, HATEOAS links are `http://…:8081` and the browser blocks them as mixed content. Relies on the gateway sending `X-Forwarded-*` (confirmed). |
| H2 in a **separate server-mode container**                  | Database as a first-class service with its own healthcheck and named volume; backend connects via `tcp://db:9092`. |
| `ddl-auto=update` + idempotent seed + named volume          | Data seeded once and **persists** across restarts/recreation.                                                      |
| Direct browser→API calls (no reverse proxy)                 | Consequence of the two-port architecture; a future hardening step is an nginx `/api` proxy exposing a single port. |
| H2 server image `2.2.224` vs backend driver `2.4.x`         | Minor version skew; verified working.                                                                              |
| Known data issue: a `GET /items/{id}` returns 404           | An orphaned item reference in the seed data; the rest of the UI renders. Cosmetic, unrelated to the deployment.    |

---

## 14. Housekeeping

- `docker-compose.override.yml` (local build-and-run helper) and the `data/` folder must **not** be
  committed — add `docker-compose.override.yml` to `.gitignore` (`data/` already is).
- Test images tagged `local-test` can be deleted from Harbor (web UI) **after** the final
  SHA-tagged images are deployed — coordinate with the team first (shared Harbor project).
- Disable the noVNC console and external access in the panel when not in use (§8.1).